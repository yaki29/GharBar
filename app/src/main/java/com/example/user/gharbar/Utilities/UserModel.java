package com.example.user.gharbar.Utilities;

import android.content.Context;
import android.util.Log;

import com.cloudant.sync.documentstore.ConflictException;
import com.cloudant.sync.documentstore.DocumentBodyFactory;
import com.cloudant.sync.documentstore.DocumentException;
import com.cloudant.sync.documentstore.DocumentRevision;
import com.cloudant.sync.documentstore.DocumentStore;
import com.cloudant.sync.documentstore.DocumentStoreException;
import com.cloudant.sync.documentstore.DocumentStoreNotOpenedException;
import com.cloudant.sync.replication.Replicator;
import com.cloudant.sync.replication.ReplicatorBuilder;
import com.example.user.gharbar.Activities.LoginActivity;
import com.example.user.gharbar.Models.User;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 1/10/17.
 */

public class UserModel {


    private static final String DOCUMENT_STORE_DIR = "data";
    private static final String DOCUMENT_STORE_NAME = "tasks";

    private DocumentStore mDocumentStore;

    private Replicator mPushReplicator;
    private Replicator mPullReplicator;
    private final Context mContext;
   // private final Handler mHandler;
    private LoginActivity mListener;

    public UserModel(Context context) {

        this.mContext = context;

        // Set up our tasks DocumentStore within its own folder in the applications
        // data directory.
        File path = this.mContext.getApplicationContext().getDir(
                DOCUMENT_STORE_DIR,
                Context.MODE_PRIVATE
        );

        try {
            this.mDocumentStore = DocumentStore.getInstance(new File(path, DOCUMENT_STORE_NAME));
        } catch (DocumentStoreNotOpenedException e) {
            Log.e("UserModel", "Unable to open DocumentStore", e);
        }

        Log.d("UserModel", "Set up database at " + path.getAbsolutePath());




    }


    public User createDocument(User user) {
        DocumentRevision rev = new DocumentRevision();
        rev.setBody(DocumentBodyFactory.create(user.asMap()));
        try {
            DocumentRevision created = this.mDocumentStore.database().create(rev);
            return User.fromRevision(created);
        } catch (DocumentException de) {
            return null;
        } catch (DocumentStoreException de) {
            return null;
        }
    }

    public void startPushReplication() {
        if (this.mPushReplicator != null) {
            this.mPushReplicator.start();
        } else {
            throw new RuntimeException("Push replication not set up correctly");
        }
    }
    private URI createServerURI()
            throws URISyntaxException {
        // We store this in plain text for the purposes of simple demonstration,
        // you might want to use something more secure.

        String username = LoginActivity.SETTINGS_CLOUDANT_USER;
        String dbName = LoginActivity.SETTINGS_CLOUDANT_DB;
        String apiKey =LoginActivity.SETTINGS_CLOUDANT_API_KEY;
        String apiSecret = LoginActivity.SETTINGS_CLOUDANT_API_SECRET;
        String host = username + ".cloudant.com";

        // We recommend always using HTTPS to talk to Cloudant.
        return new URI("https", apiKey + ":" + apiSecret, host, 443, "/" + dbName, null, null);
    }

    public void reloadReplicationSettings(int flag)
            throws URISyntaxException {

        // Stop running replications before reloading the replication
        // settings.
        // The stop() method instructs the replicator to stop ongoing
        // processes, and to stop making changes to the DocumentStore. Therefore,
        // we don't clear the listeners because their complete() methods
        // still need to be called once the replications have stopped
        // for the UI to be updated correctly with any changes made before
        // the replication was stopped.
        this.stopAllReplications();

        // Set up the new replicator objects
        URI uri = this.createServerURI();


        mPullReplicator = ReplicatorBuilder.pull().to(mDocumentStore).from(uri).build();
        mPushReplicator = ReplicatorBuilder.push().from(mDocumentStore).to(uri).build();

        mPushReplicator.getEventBus().register(this);
        mPullReplicator.getEventBus().register(this);
        if(flag==0)
        startPushReplication();
        else if (flag==1)
            startPullReplication();

        Log.d("UserModel", "Set up replicators for URI:" + uri.toString());
    }
    public void stopAllReplications() {

        if (this.mPushReplicator != null) {
            this.mPushReplicator.stop();
        }
        if (this.mPullReplicator != null) {
            this.mPullReplicator.stop();
        }
    }
    public List<User> allTasks() throws DocumentStoreException {
        int nDocs = this.mDocumentStore.database().getDocumentCount();
        List<DocumentRevision> all = this.mDocumentStore.database().read(0, nDocs, true);
        List<User> tasks = new ArrayList<User>();

        // Filter all documents down to those of type Task.
        for(DocumentRevision rev : all) {
            User t = User.fromRevision(rev);
            if (t != null) {
                tasks.add(t);
            }
        }

        return tasks;
    }

    public void startPullReplication() {
        if (this.mPullReplicator != null) {
            this.mPullReplicator.start();
        } else {
            throw new RuntimeException("Push replication not set up correctly");
        }
    }

    public void setReplicationListener(LoginActivity loginActivity) {
        this.mListener = loginActivity;
    }

    public User updateDocument(User task) throws ConflictException, DocumentStoreException {
        DocumentRevision rev = task.getDocumentRevision();
        rev.setBody(DocumentBodyFactory.create(task.asMap()));
        try {
            DocumentRevision updated = this.mDocumentStore.database().update(rev);
            return User.fromRevision(updated);
        } catch (DocumentException de) {
            return null;
        }
    }
}

package net.twerion.skycrystals.database;

import net.twerion.skycrystals.SkyCrystals;

import java.util.ArrayList;
import java.util.List;

public abstract class DatabaseUpdate {

    private boolean update;
    private boolean ready;
    private boolean forceUpdate;
    private List<ReadyExecutor> readyExecutors;

    public DatabaseUpdate() {
        this.readyExecutors = new ArrayList<>();
        this.forceUpdate = false;
    }

    public List<ReadyExecutor> getReadyExecutors() {
        return this.readyExecutors;
    }

    public void addReadyExecutor(ReadyExecutor executor) {
        if(this.ready) {
            executor.ready();
            return;
        }
        this.readyExecutors.add(executor);
    }

    public boolean isUpdate() {
        return this.update;
    }

    public void setUpdate(boolean update) {
        this.update = update;
    }

    public void setReady(boolean ready) {
        this.ready = ready;
        if(ready) {
            for(ReadyExecutor executor : this.readyExecutors) {
                executor.ready();
            }
            this.readyExecutors.clear();
        }
    }

    public void setForceUpdate(boolean forceUpdate) {
        this.forceUpdate = forceUpdate;
    }

    public boolean isForceUpdate() {
        return this.forceUpdate;
    }

    public void addToUpdater() {
        SkyCrystals.getInstance().getSQLManager().getUpdater().addToUpdater(this);
    }

    public void removeFromUpdater() {
        SkyCrystals.getInstance().getSQLManager().getUpdater().removeFromUpdater(this);
    }

    public abstract void saveData();

    public abstract void saveDataAsync();

    public abstract void loadData();

    public abstract void loadDataAsync();

    public static abstract interface ReadyExecutor {
        public abstract void ready();
    }
}

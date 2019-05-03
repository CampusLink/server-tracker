package com.campus.tracker;

import com.campus.system.ServiceContext;
import com.campus.system.ServiceMenu;
import com.campus.system.annotation.Service;
import com.campus.system.storage.StorageService;
import com.campus.system.storage.model.Box;
import com.campus.system.storage.model.BoxQuery;
import com.campus.system.storage.model.BoxStore;
import com.campus.system.storage.model.StorageType;
import com.campus.system.token.TokenService;
import com.campus.system.token.model.Token;
import com.campus.system.tracker.TrackerService;
import com.campus.system.tracker.model.Tracker;

import java.util.List;

@Service(name = ServiceMenu.TRACKER, module = "Tracker")
public class TrackerServiceImpl extends TrackerService {
    private StorageService mStorageService;
    private TokenService mTokenService;
    private BoxStore mBoxStore;
    private final String storeName = "tracker";
    private final String userName = "tracker";
    private final String password = "tracker";
    private Box<Tracker> mBox;

    public void init(ServiceContext serviceContext) {
        mTokenService = (TokenService) serviceContext.getSystemService(ServiceMenu.TOKEN);
        mStorageService = (StorageService) serviceContext.getSystemService(ServiceMenu.STORAGE);
        mBoxStore = mStorageService.obtainBoxStore(StorageType.MySql, storeName, userName, password);
        mBox = mBoxStore.boxFor(Tracker.class);
    }

    public void insertTracker(String s, Tracker tracker) {
        mBox.put(tracker);
    }

    public void insertPatchTracker(String s, List<Tracker> list) {
        mBox.put(list);
    }

    public List<Tracker> queryTrackersByLayout(String token, String layout, long preId, int pageSize) {
        Token token1 = mTokenService.parseToken(token);
        if (token1 == null) {
            return null;
        }
        //TODO 判断当前token对应的用户是否是管理员
        BoxQuery<Tracker> query = mBox.obtainQuery();
        query.whereEqualTo("layout", layout);
        if (preId > 0) {
            query.whereGreaterThan("ID", preId);
        }
        return query.orderByDESC("createTime")
                .limit(pageSize)
                .query();
    }

    public List<Tracker> queryTrackersByItem(String token, String layout, String item, long preId, int pageSize) {
        Token token1 = mTokenService.parseToken(token);
        if (token1 == null) {
            return null;
        }
        BoxQuery<Tracker> query = mBox.obtainQuery();
        query.whereEqualTo("layout", layout)
                .whereEqualTo("item", item);
        if (preId > 0) {
            query.whereGreaterThan("ID", preId);
        }
        return query.orderByDESC("createTime")
                .limit(pageSize)
                .query();
    }

    public List<Tracker> queryTrackersByOperate(String token, String layout, String item, String operate, long preId, int pageSize) {
        Token token1 = mTokenService.parseToken(token);
        if (token1 == null) {
            return null;
        }
        BoxQuery<Tracker> query = mBox.obtainQuery();
        query.whereEqualTo("layout", layout)
                .whereEqualTo("item", item)
                .whereEqualTo("operate", operate);
        if (preId > 0) {
            query.whereGreaterThan("ID", preId);
        }
        return query.orderByDESC("createTime")
                .limit(pageSize)
                .query();
    }
}

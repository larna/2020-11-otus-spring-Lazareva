db.auth('root', '12345');
rs.initiate(
    {_id: "rs0", version: 1,
        members: [
            { _id: 0, host : "mongo-primary:27017" },
            { _id: 1, host : "mongo-secondary:27017" },
            { _id: 2, host : "mongo-arbiter:27017" }
        ]
    }
);

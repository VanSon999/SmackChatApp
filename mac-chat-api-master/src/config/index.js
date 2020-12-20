import mongodb from 'mongodb';

export default {
  // "port": 3005,
  // "mongoUrl": "mongodb://localhost:27017/chat-api",
  "port": process.env.PORT,
  "mongoUrl": "mongodb+srv://user:01213512168@mogodbnosql.drgff.mongodb.net/smackChat?retryWrites=true&w=majority",
  "bodyLimit": "100kb"
}

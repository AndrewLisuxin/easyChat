# easyChat
develop a simple java chat application

Chat Model: use Mediator design pattern

Client: 
1. connect to the Server
2. send message received from GUI to the server(Event-driven model)
3. use a seperate thread to continuously receive message from the server.
4. a user can be involved in mutiple chat at the same time.

Server: 
1. for every client connection, use a ServerThread to handle read-write on this socket. 
2. A client only has one connection to the server, and every Client Thread transmit message from this client to target client Thread or Chat room.
3. every Chat use a seperate thread to continuously broadcast chat message to every member of this Chat (handle individual chat and group chat in the same way).
4. if users list or group chat list changes, the server will push this update to every client

Individual chat:
1. a user can send an invition to another user. If the other one accepts it, then the individual chat is built and they can chat.

Group Chat:
1. a user can create groups
2. any use can join any group without permission check


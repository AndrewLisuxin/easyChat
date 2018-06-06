# easyChat
develop a simple java chat application

1. chat model: Mediator design pattern
2. server-client communication: TCP socket

Client: 
1. implement front-end with swing
2. main thread: receive information from GUI, pack to message and send it to the server(Event-driven model)
3. use a seperate thread to continuously receive message from the server.
4. a user can be involved in mutiple chat at the same time.

Server: 
1. for every client connection, use a ServerThread to handle read-write on this socket. 
2. A client has only one connection to the server, and every ServerThread transmit messages from this client to target client or push it to the message queue of target Chat.
3. every Chat use a seperate thread to continuously take messages from message queue and broadcast it to every member of this Chat (handle individual chat and group chat in the same way).
4. if users list, group chats list or chat members list changes, the server will push this update to relative clients

Message:
1. abstract class Message is the super class for all specific message classes
2. a kind of specific message class represents a kind of message, like chat message, invitation message, join group message and
so on.

Individual chat:
1. a user can send an invition to another user. If the other accepts it, then the individual chat is built and they can chat.

Group Chat:
1. a user can create groups
2. any client can join any group without permission check

Additional functions:
1. support files upload and download


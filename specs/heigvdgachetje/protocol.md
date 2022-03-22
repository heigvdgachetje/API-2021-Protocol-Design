# RES/API - Labo 03

## Protocol specification

### Protocol objectives: what does the protocol do?
The protocol enables structured communication between a client and a server.
A client should be able client to ask a server to compute a calculation and get the result in return.
It specifies:
- The transport layer protocol used
- The IP address and port used by the server
- The structure of the messages send by the client and by the server
- The reaction the server and the client should have in response to receiving a message
- The flow of the conversation

### Overall behavior

#### What transport protocol do we use?
The transport protocol used is TCP.

#### How does the client find the server (addresses and ports)?
Note: The client will run on the same machine as the server.
Server's IP address and port: 127.0.0.1:7777

#### Who speaks first?
Once the connexion is established, the server sends a welcoming message to the client.

#### Who closes the connection and when?
The server closes the connexion when a specific message from the client is received.
The client can then send requests to the server.

### Messages:

#### What is the syntax of the messages?

##### Client
The client can send the following types of messages:
- A computation request for a specific binary operation between two operands, using the syntax `[Operator] [Operand1] [Operand2]`.
- A request for help, by sending `help`.
- A request to close the connexion, by sending `exit`.

##### Server
All messages send by the server should begin with `> `.
Then, the server can send the following types of messages:
- A welcome-message, containing at least:
  - the word `welcome`
  - the syntax of a valid computation request
  - how to request help
- A response to a computation request, using the syntax `[Operand1] [Operator] [Operand2] = [Result]`.
  The `[Operator]` should be a symbol when applicable.
- A help-message, containing at least:
  - the syntax of a valid computation request
  - how to request help
  - how to exit
- An error-message, containing at least:
  - the word `error` and the erroneous request
  - how to request help

#### What is the sequence of messages exchanged by the client and the server? (flow)
1. The server sends a welcoming message.
2. The client sends a request.
3. The server responds to the request appropriately.
4. If the connexion has not been closed, go back to step 2.

#### What happens when a message is received from the other party? (semantics)

##### Client
The client can receive different types of messages:
- A welcome-, error-, or help- message
- The result of a computation request
- A malformed message

The client doesn't respond/react to any messages.

##### Server
The server reacts differently to specific types of messages:
- Computation request: the server computes the result and send it back to the client
- Help request: the server sends a help-message to the client
- Exit request: the server ends the connexion with the client
- Malformed message: the server sends an error-message to the client

### Specific elements

####Supported operations
At first, only these four basic operations will be supported:

|   Operation    | Client-side operator | Server-side operator |
|:--------------:|:--------------------:|:--------------------:|
|    addition    |        `add`         |         `+`          |
|  subtraction   |        `sub`         |         `-`          |
| multiplication |        `mul`         |         `*`          |
|    division    |        `div`         |         `/`          |

Note: if the 2 operands of a division are integers, an integer division is performed.

####Error handling
If a malformed message is received:
- by the client, it does nothing and simply ignore the message
- by the server, it sends an error message to the client.

####Extensibility
Some possible extensions:
- Adding other binary operators (`mod`, `min`, `max`, `exp`, `log`, etc.)
- Adding unary operators and functions (`neg`, `exp`, `log`, `sin`, etc.)
- Operands could be the result of a computation
- Handling of parentheses

### Examples: examples of some typical dialogs.
```
> Welcome! To request a computation, send "[Operator] [Operand1] [Operand2]". To get more information, send "help".
add 2 2
> 2 + 2 = 4
asdf
> Error! Invalid request "asdf". To get more information, send "help".
div 5 2
> 5 / 2 = 2
div 5. 2
> 5 / 2 = 2.5
mul 3 1e3
> 3 * 1000 = 3000
exit
```

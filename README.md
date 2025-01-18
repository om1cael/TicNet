
# TicNet Server

TicNet is a multiplayer Tic-Tac-Toe game server built in Java. It allows players to connect to a server, create game rooms, join existing ones, and play Tic-Tac-Toe against each other. The server handles multiple connections concurrently using threading and provides real-time game interaction.

## Features

- **Player Connections**: Supports multiple players connecting simultaneously.
- **Game Rooms**: Allows players to create and join Tic-Tac-Toe game rooms.
- **Real-Time Gameplay**: Players can make moves and interact in real-time.
- **Logging**: Logs key actions and events for monitoring the server's status.

## Requirements

- Java 8 or later
- Gradle (for building the project)

## Getting Started

### 1. Clone the repository

```bash
git clone https://github.com/om1cael/TicNet.git
cd TicNet
```

### 2. Build the Project

To build the project, run the following Maven command:

```bash
./gradlew build
```

### 3. Running the Server

To start TicNet, use the following command:

```bash
./gradlew run
```

The server will start on port `1024` by default. You can configure the port and thread settings by modifying the `Main` class.

### 4. Connecting to the Server

Players can connect to the server using any compatible client. The server will listen on port `1024` by default. Once connected, players can join or create game rooms to start playing.

## Code Structure

### Main.java
The entry point for the TicNet server. It handles the initialization of the server socket, managing player connections, and creating threads to handle client communication.

### RoomManager.java
Manages the creation, joining, and deletion of game rooms. It keeps track of active games and players involved in each game.

### Player.java
Represents a player on the server. This class handles communication with the client, reads player commands, and processes actions such as creating rooms, joining rooms, and making moves in the game.

### Commands
- **create-room**: Creates a new game room.
- **join-room <host id>**: Joins an existing game room.
- **make-move <row> <column>**: Handles a player's move in the game.

### Responses
The server sends a lot of "responses" to the client, just like HTTP. Varying from ID to game updates (like board moves), they are sent in JSON and the client must update their front-end based on those. All of the responses can be found within the [response package](https://github.com/om1cael/TicNet/tree/master/src/main/java/com/om1cael/ticnet/response), along with their models.

Example (board update response):
```
{
  "code": "BR_600",
  "symbol": "x",
  "row": 1,
  "column": 2
}
```

> [!NOTE]
> The server sends a response with the player's ID upon connection. This can be found on the [Player class](https://github.com/om1cael/TicNet/blob/master/src/main/java/com/om1cael/ticnet/network/Player.java).

## Contributing

Contributions are welcome! If you'd like to contribute, please fork the repository, create a new branch, and submit a pull request with your changes.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.


# Gold-Miner-Heuristic-Agent
A requirement for CS Intelligent Systems course. A take on the A-Star algorithm for the pathfinding agent with self-calculated heuristics. (Note: Will not work properly on stock windows terminals due to the use of coloured ascii characters. Might want to use either Repl or IntelliJ as a simple IDE to test)

The gist is that there's a gold block the miner must reach. The agent does not know where it is and can scan ahead of it to detect if it finds something. When it scans, it returns what that object is (Pit/Beacon/Gold) but it doesn't know where in that line it is.

The agent can also ONLY turn right and move forward.

The beacon helps return a value of where the gold is from the beacon in terms of Manhattan Distance, which makes the agent calculate a grid of squares where the gold might be.

Pits are tiles that makes the game over. The agent must avoid these tiles in order to reach the goal state.

Currently, it's having difficulties with beacon calculations due to possible pits it needs to avoid, making it stuck in an endless loop.

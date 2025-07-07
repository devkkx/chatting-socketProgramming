Java Chat Application with Login and Registration
--------------------------------------------------

Description:
-------------
This is a Java-based desktop chat application developed using Swing GUI and Socket programming.
It includes a secure Login and Registration system, allowing users to authenticate and participate
in real-time chat after successful login. The interface is designed for clarity and eye comfort,
making it ideal for both learning and demonstration.

Features:
----------
- Login and Registration screens with user input validation
- Real-time multi-user chat using socket communication
- Interactive GUI built with Java Swing
- Professional, eye-friendly UI with pastel theme colors
- Dynamic user join and exit notifications
- Option to switch between Login and Registration interfaces easily

File Descriptions:
-------------------
- Server.java      : Accepts connections and manages client threads
- Client.java      : Chat client interface with message send/receive
- Login.java       : Handles user login with credential validation
- Register.java    : Allows new users to register accounts
- ChatApp.java     : Optional launcher for the application
- send.png         : Icon image used for the Send button in Client interface

Notes:
-------
- GUI uses pink and soft blue tones for improved user experience
- Error messages and status prompts are displayed using dialog boxes
- Client connects to localhost on port 12345 by default
- Backend logic includes form validation and connection feedback

Improvements & Future Additions:
---------------------------------
✓ Encrypt passwords before storing them (e.g., using BCrypt)  
✓ Add chat history loading from the database  
✓ Enable private messaging (1-to-1 chat)  
✓ Display message timestamps in chat window  
✓ Improve responsiveness for smaller screens  
✓ Add emoji or file-sharing support  
✓ Add admin/moderator roles and permissions  
✓ Add user profile editing (e.g., change password)  
✓ Add group chat rooms or channels  
✓ Add dark mode / theme toggle feature

Author: Kshirod Sahoo  
Year: 2025

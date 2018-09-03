
OAuth2 abstract flow: 


1. Third-party website client notifies Authorization server that it wants to access a user's resource (ex. a certain api path /api/users GET)

2. Authorization server then asks the user to log in first

3. then Authorization asks the user whether he/she wants to allow / deny this  Third-party website client to access his/her resource

4. If the user allows, the Authorization server will send an authorization grant code to the third-party website client 

5. the third-party website client will then use the authorization grant code, clientId, and secret to authentication against the authorization server

6. If auththenticated, the third-party website client will get the ultimate access token and the authorities he gets to do.

7. With that access token in hand, the third-party website client can invoke those APIS to get what it wants, such as the user's information.


References: 
Three-legged OAuth flow:
https://www.ibm.com/support/knowledgecenter/en/SS9H2Y_7.7.0/com.ibm.dp.doc/oauth_threeleggedflow.html

pring Security OAuth2 Provider - minimum inplementation: 
http://rensanning.iteye.com/blog/2384996


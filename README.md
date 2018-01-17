# FTP

**Beware of the file paths used in the codes**  
*They're hard coded*


1. Finished threading in Client.java  
2. client can have one upload and one download request concurrently  
3. No hashmap, no nothing used  


Client gives request and wait for server to response and then read FileByte class.
Client can in this time request for another service(if previouly download then now upload).
And the file size must be less than Integer.MAX()-10 (but in practise, this value differs from pc to pc)
cause although vector can allocate memory dynamically, while running the program, it catches 
"java.lang.OutOfMemoryError: Java heap space" error. 


Work to be done:  
Now, the two static var downloadRequest and uploadRequest in GetServed class is to be used to
refrain the client from giving two request of same king at a time. Client will be able to send 
request but new threads will not be created until previous threads are closed.


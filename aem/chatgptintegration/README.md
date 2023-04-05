# ChatGPT Integration

Enable ChatGPT integation from AEM components tool for to allow content authors to generate the content quickly.

## Modules

The changes:

* core: com.chatgpt.core.servlets.ChatServlet - the servlet invokes chatgpt API with user provided prompt
* ui.apps: /apps/chatgptintegration/clientlibs/editor/sites/page/hook/chatgpt - Editor hook to enable new edit tool bar action to title component(allows user to enter the prompts and generate the reponse)

## Images

![alt text](https://github.com/techforum-repo/images/blob/main/AEM-chatgpt-integration1.jpg?raw=true)
![alt text](https://github.com/techforum-repo/images/blob/main/AEM-chatgpt-integration2.jpg?raw=true)
![alt text](https://github.com/techforum-repo/images/blob/main/AEM-chatgpt-integration3.jpg?raw=true)
![alt text](https://github.com/techforum-repo/images/blob/main/AEM-chatgpt-integration4.jpg?raw=true)
![alt text](https://github.com/techforum-repo/images/blob/main/AEM-chatgpt-integration5.jpg?raw=true)


## How to build

Update the Open AI API token in the ChatServlet, the API token can be reterived from ChatGPT console

![alt text](https://github.com/techforum-repo/images/blob/main/chatgpt-api-keys.jpg?raw=true)

To build all the modules and deploy the `all` package to a local instance of AEM, run in the project root directory the following command:

    mvn clean install -PautoInstallSinglePackage
	
## Blog Refernce

https://medium.com/p/312651291713


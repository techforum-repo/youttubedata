# Web Component to include remote HTML without using IFRAME

I am trying find a best alternative to iframe to embed remote HTML pages into my website to avoid some of the issues related to IFRAME

Trying to use HTML Web Component with fetch to reterive the remote HTML and embed the HTML into the page

## Testing

From root folder execute the below command - this will start the server with the HTML page that is going embed the remote HTML
```python3 -m http.server 80```

cd to target folder and execute the below command - this will start the server with the target HTML page
```python3 server.py```

Access
```http://localhost/html-include-demo.html``` this will embed ```http://localhost:8080/target.html```

Some of the know issues

* the external Java Script/CSS loaded into the target.html not working while embeding the page through shadow DOM (Still exploring)
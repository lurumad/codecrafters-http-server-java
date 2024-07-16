# HTTP Server Project

## Overview

In this stage, your server will extract the URL path from an HTTP request and respond with either a 200 or 404, depending on the path.

## HTTP Request Structure

An HTTP request is made up of three parts, each separated by a CRLF (`\r\n`):

1. Request line.
2. Zero or more headers, each ending with a CRLF.
3. Optional request body.

### Example HTTP Request

```
GET /index.html HTTP/1.1\r\n
Host: localhost:4221\r\n
User-Agent: curl/7.64.1\r\n
Accept: */*\r\n
\r\n
```

### Breakdown of the Request

**Request Line**

- `GET`                          // HTTP method
- `/index.html`                  // Request target
- `HTTP/1.1`                     // HTTP version
- `\r\n`                         // CRLF that marks the end of the request line

**Headers**

- `Host: localhost:4221\r\n`     // Header that specifies the server's host and port
- `User-Agent: curl/7.64.1\r\n`  // Header that describes the client's user agent
- `Accept: */*\r\n`              // Header that specifies which media types the client can accept
- `\r\n`                         // CRLF that marks the end of the headers

**Request Body (empty)**

The "request target" specifies the URL path for this request. In this example, the URL path is `/index.html`.

Note that each header ends in a CRLF, and the entire header section also ends in a CRLF.

## Tests

The tester will execute your program like this:

```sh
$ ./your_server.sh
```

The tester will then send two HTTP requests to your server.

### Test 1: 404 Response

First, the tester will send a GET request, with a random string as the path:

```sh
$ curl -v http://localhost:4221/abcdefg
```

Your server must respond to this request with a 404 response:

```
HTTP/1.1 404 Not Found\r\n\r\n
```

### Test 2: 200 Response

Then, the tester will send a GET request, with the path `/`:

```sh
$ curl -v http://localhost:4221
```

Your server must respond to this request with a 200 response:

```
HTTP/1.1 200 OK\r\n\r\n
```

## Notes

- You can ignore the headers for now. You'll learn about parsing headers in a later stage.
- In this stage, the request target is written as a URL path. But the request target actually has four possible formats. The URL path format is called the "origin form," and it's the most commonly used format. The other formats are used for more niche scenarios, like sending a request through a proxy.
- For more information about HTTP requests, see the [MDN Web Docs on HTTP requests](https://developer.mozilla.org/en-US/docs/Web/HTTP/Messages) or the [HTTP/1.1 specification](https://www.w3.org/Protocols/rfc2616/rfc2616-sec5.html).
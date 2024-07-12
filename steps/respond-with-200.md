# Task: Respond to an HTTP Request with a 200 Response

## Overview
In this stage, your server will respond to an HTTP request with a 200 response. An HTTP response is made up of three parts, each separated by a CRLFString (`\r\n`):

1. Status line.
2. Zero or more headers, each ending with a CRLFString.
3. Optional response body.

In this stage, your server's response will only contain a status line.

## Expected Response

Your server must send the following response:

```
HTTP/1.1 200 OK\r\n\r\n
```

### Breakdown of the Response

- **Status line:**
  - `HTTP/1.1`  - HTTP version
  - `200`       - Status code
  - `OK`        - Optional reason phrase
  - `\r\n`      - CRLFString that marks the end of the status line

- **Headers:**
  - (empty)
  - `\r\n`      - CRLFString that marks the end of the headers

- **Response body:**
  - (empty)

## Testing Your Server

To test your server, the tester will execute your program as follows:

```
$ ./your_server.sh
```

The tester will then send an HTTP GET request to your server:

```
$ curl -v http://localhost:4221
```

Your server must respond to the request with the following response:

```
HTTP/1.1 200 OK\r\n\r\n
```

## Notes

- You can ignore the contents of the request for this stage. Parsing requests will be covered in later stages.
- For more information about HTTP responses, refer to the [MDN Web Docs on HTTP responses](https://developer.mozilla.org/en-US/docs/Web/HTTP/Messages#responses) or the [HTTP/1.1 specification](https://tools.ietf.org/html/rfc2616).

This challenge uses HTTP/1.1.
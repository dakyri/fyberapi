# fyberapi

There is a flag in FyberAPI which can be set true to enable mocking, 
of the http response (set via setMockedResponse).
Set this to 'false' to enable actual communication (which it is by default).
with the server, which is currently serving up a complaint about
bad timestamps to all requests. 
Also, for unit testing, pass the parameter  fetchGooApis on FyberAPI(..) as false. The
Google As id needs be loaded asynchronously, and this is problematic for testing.

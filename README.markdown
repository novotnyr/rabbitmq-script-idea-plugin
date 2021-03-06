About
=====
This is a RabbitMQ Scripting Support for IntelliJ IDEA and related
JetBrains IDE.

Requirements
============

The scripting plugin runs against REST API provided by RabbitMQ.

How To Use
==========
Create a YAML file with a `.rabbitmq` extension that represents a script which can be executed against RabbitMQ broker.

Declare a configuration and one or more script command. Each script command can be run from IDE independently.

Screenshot
----------

![Screenshot](screenshot.png)

Sample Script
-------------

    ---
    host: localhost
    port: 15672
    user: guest
    password: guest
    protocol: http
    vhost: /
    ---
    publish: amq.default
    routing-key: cabbage
    description: Notify a truckload of veggies
    headers:
      X-Correlation-ID: 12cba88f
      X-Domain: vegetables
    json: >
      {
          "message" : "A truckload of veggies has arrived"
      }
    reply-to: reply

The script consists of multiple YAML documents.

*    first document represents a an optional *configuration* that is applied for the subsequent commands.
*    subsequent YAML document correspond to *script commands*

Script Syntax
=============

Configuration
-------------

* `host`: hostname of RabbitMQ installation
* `user`: RabbitMQ user used for authentication
* `port`: port that is used to reach the RabbitMQ API. Optional. Can be deduced from protocol.
* `password`: RabbitMQ password used for authentication
* `vhost`: RabbitMQ Virtual Host used for connections
* `protocol`:
    * `HTTP` or `http`: This is using RabbitMQ REST API exposed by [RabbitMQ `management` plugin](https://www.rabbitmq.com/management.html). It is implicitly associated with 15672 port.
    * `HTTPS` or `https`: SSL/TLS based version of HTTP protocol. It is implicitly associated with 15671 port.

Configuration can be omitted. However, it is necessary to provide an empty document!

This is an example of a minimalistic script with implicit configuration.
Please observe a double line indicating an empty first document.

    ---
    ---
    get: cabbage

`publish` Command
-----------------

### Example

    ---
    publish: amq.default
    routing-key: cabbage
    description: Notify a truckload of veggies
    json: >
      {
          "message" : "Message number five"
      }
    headers:
      X-Correlation-ID: 12cba88f
      X-Domain: vegetables
    reply-to: reply


* `publish`: name of the exchange that will receive a message for further routing
* `routing-key`: routing key used with the message publication
* `description`: an arbitrary comment-like human-readable text
* `json`: provide a JSON payload. Message will be sent with `application/json` content type
* `payload`: an alternative to `json` key. A string message will be sent.
* `headers`: a map of message headers and values. Please note that these are *headers*, not *properties*!
* `reply-to`: indicates a RPC message. This is mapped to `reply_to` property of the AMQP protocol.

`get` Command
-------------

### Example

    ---
    get: cabbage

* `get`: name of the queue used to consume the message
* `description`: an arbitrary comment-like human-readable text

The message will be consumed with acknowledgement and no requeuing by default.

`binding` Command: Declare Binding
----------------------------------

```yaml
binding:
  exchange: food
  queue: cabbage
  routing-key: cabbage
```

* `exchange`: name of the exchange to bind from
* `queue`: name of the queue to bind to
* `routing-key`: associated with binding


`exchanges` Command: List Exchanges
-----------------------------------

```yaml
exchanges:
  vhost: /
```

* `vhost` (optional): list exchanges of the specified virtual host. If
left out, all relevant exchanges are listed

Profiles
========
Each script can be run with an explicit configuration profile. This profile
can be configured in IDE Preferences (*Other Settings* | *RabbitMQ Script*).

You may configure one or more profiles with individual host, user and further
configuration.

When opening a script, you can pick an active profile from a combobox on the
top of the editor window.
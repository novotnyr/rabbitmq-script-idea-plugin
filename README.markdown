About
=====
This is a RabbitMQ Scripting Support for IntelliJ IDEA and related
JetBrains IDE.

Requirements
============

The scripting plugin runs against REST API provided by RabbitMQ. Currently, only version 3.6.x is supported for message retrievals via `get`. Version 3.7.x is supported for message publications.

How To Use
==========
Create a YAML file that represents a script which can be executed against RabbitMQ broker.

Declare a configuration and one or more script command. Each script command can be run from IDE independently.

Screenshot
----------

![Screenshot](screenshot.png)

Sample Script
-------------

    ---
    host: localhost
    user: guest
    password: guest
    protocol: http
    vhost: /
    ---
    publish: amq.default
    routing-key: cabbage
    json: >
      {
          "message" : "Message number five"
      }
    reply-to: reply

The script consists of multiple YAML documents.

*    first document represents a mandatory *configuration* that is applied for the subsequent commands.
*    subsequent YAML document correspond to *script commands*

Script Syntax
=============

Configuration
-------------

* `host`: hostname of RabbitMQ installation
* `user`: RabbitMQ user used for authentication
* `password`: RabbitMQ password used for authentication
* `vhost`: RabbitMQ Virtual Host used for connections
* `protocol`: currently, only `http` is supported. This is using RabbitMQ REST API exposed by [RabbitMQ `management` plugin](https://www.rabbitmq.com/management.html)

`publish` Command
-----------------

### Example

    ---
    publish: amq.default
    routing-key: cabbage
    json: >
      {
          "message" : "Message number five"
      }
    reply-to: reply


* `publish`: name of the exchange that will receive a message for further routing
* `routing-key`: routing key used with the message publication
* `json`: provide a JSON payload. Message will be sent with `application/json` content type
* `payload`: an alternative to `json` key. A string message will be sent.
* `reply-to`: indicates a RPC message. This is mapped to `reply_to` property of the AMQP protocol.

`get` Command
-------------

### Example

    ---
    get: cabbage

* `get`: name of the queue used to consume the message

The message will be consumed with acknowledgement and no requeuing by default.
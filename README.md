


# My Personal Kanban - Local Cloud


A Clojure application that enables My Personal Kanban to be stored and portable with your own dedicated server.

The application exposes REST Api on specified port. My Personal Kanban will use that API to send Kanban to be persisted.

There is no need to generate Kanban key. Any string value which is at least 1 character long will work. However, using the same key as your Cloud key will give you possibility of storing your Kanban in multiple places, as required.


## Usage

### Installation

Download standalone version of my-personal-kanban-local-cloud jar file. It requires Java installed to run.
You can get it from [http://dl.bintray.com/greggigon/maven/my-personal-kanban-local-cloud-0.1.0-standalone.jar][1].

### Running

Start the server by executing:

    java -jar my-personal-kanban-local-cloud-{VERSION}-standalone.jar

Sever could be configured with two options:

* port - port which will be used to run server (default *8080*)
* directory - place on file system where Kanbans will be stored (default */user-home-folder/mpk*)

To change defaults run server with:

    java -jar my-personal-kanban-local-cloud-{VERSION}-standalone.jar port 8888 directory /home/gregster/awesome

## Contributing

If you wish to contribute the code, please feel free to fork and crate pull requests via GitHub.


## License

Copyright © 2014 Grzegorz (Greg) Gigon, My Personal Kanban

Distributed under the GNU GENERAL PUBLIC LICENSE.


  [1]: https://bintray.com/greggigon/maven/My-Personal-Kanban-Server/0.1.0/view/general

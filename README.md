
   H E L L O F E N I X

   Introduction to the fenix framework


   1) create database:

      $ mysql -p -u root

      Enter password: rootroot

      mysql> CREATE DATABASE hello;

      musql> \q

   2) clone and build:

      $ git clone https://github.com/tecnico-softeng/hellofenix.git

      $ cd hellofenix

      $ mvn package

   3) execute: add names and fetch the saved data

      $ mvn exec:java -Dexec.args="Pedro Mateus Tiago Maria"

      $ mvn exec:java

   4) execute: remove and add names

      $ mvn exec:java -Dexec.args="-Mateus -Maria +Joaquim +Teresa"

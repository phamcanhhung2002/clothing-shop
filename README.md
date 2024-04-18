# Clothe Shop

> A web application for selling clothes using ReactJS and Spring Boot. <!-- If you have the project hosted somewhere, include the link here. -->

## Table of Contents

* [Technologies Used](#technologies-used)
* [Features](#features)
* [Setup](#setup)
* [Usage](#usage)
* [Acknowledgements](#acknowledgements)
<!-- * [License](#license) -->

## Technologies Used

* ReactJS

* Spring Boot

* PostgreSQL

* MongoDB

## Features

### Customer

* View clothing categories and products.

* View the details of a product and add it to the cart.

* Pay for the cart using the Stripe payment service and view the order history.

### Admin

* View all users of the shop; view, edit, and create users' personal information.

* View all products in the shop; view, edit, and create products' details.

* View the statistical information on orders based on time.

## Setup

Before you use this project, make sure that you:

* Installed Java 21.

* Installed NodeJS 20.

* Installed PostgreSQL 12.

* Installed MongoDB/ Have an MongoDB Atlas account.

* Had a Firebase account - to have all Firebase api keys listed in _admin/src/firebase.js_ file.

* Had a Stripe account - to have the public key and private key.

Then you need to create four _.env_ files based on three _.env.example_ files in each subfolder.

* The _api_ folder has two _.env_ for the _main_ and _test_ folders.

* Place them in _client_, _admin_, _api/src/main/resources/_ and _api/src/test/resources/_.

* Paste all relevant keys to them.

## Usage

To run the project:

* Go to the _api_ folder, run:

```bash
./mvnw clean verify
```

```bash
java -jar target/api-0.0.1-SNAPSHOT.jar
```

* Next, go to the _client_ folder, run:

```bash
yarn install
```

```bash
yarn start
```

* Finally, go to the __admin__ folder, and run:

```bash
yarn install
```

```bash
yarn start
```

After all servers have run, you can seed the database by typing this URL into the browser:

```url
http://localhost:8080/api/stat/seed
```

Wait for several seconds until you get the message "Seed successfully!".

You could experience this website with accounts listed in _api/src/main/java/com/clothingshop/api/domain/seed/seed.json_.

The test card number and date for Stripe are 4242 4242 4242 4242 and 8/24.

## Explaination of Order Analytics Feature

### Requirement

Assuming you have a list of order entries, each entry contains a timestamp indicating the moment when the corresponding order was placed. You want to know, in a specific range of time and the time interval (e.g., hour, day, or month), how many orders there are in each interval in that range.

Example:

* Orders:

```JSON
[
    {
        "id": 1,
        "createdAt" : "2024-04-14T17:00:00.000+00:00"
    },
    {
        "id": 2,
        "createdAt" : "2024-04-1516:30:00.000+00:00"
    },
    {
        "id": 3,
        "createdAt" : "2024-04-17T19:25:00.000+00:00"
    },
    {
        "id": 4,
        "createdAt" : "2024-04-18T00:30:00.000+00:00"
    }
]
```

* Start date: 2024-04-15.

* End date: 2024-04-19.

* Timezone: +07:00.

* Interval type: day.

* Expected result:

```JSON
[
    {
        "interval": 15,
        "quatity": 2
    },
    {
        "interval": 16,
        "quatity": 0
    },
    {
        "interval": 17,
        "quatity": 0
    },
    {
        "interval": 18,
        "quatity": 2
    },
    {
        "interval": 19,
        "quatity": 0
    }
]
```

### Implement

Firstly, given the _start date_ and the _end date_, we want to filter all entries that have the creation timestamp between {_start date_}00:00.000 and {_end date_}23:59.999 in the current timezone.

* Because an order's time type is _timestamp_, which has a timezone of +00:00, we have to convert the start date and end date to the equivalent start date and end date in the +00:00 timezone.

* In the example above, the equilavent start and end dates are 2024-04-14T17:00:00.000 and 2024-04-19T16:59:59.999.

Secondly, we also need to convert the creation time of each entry to the creation time in the current timezone.

* Then, to collect entries that are in the same interval, we introduce a new attribute in each entry, such that two entries in the same interval must have the same value of this attribute.

* For instance, if we want to collect entries with an _hour_ interval, we could truncate the timestamp of an entry by masking its smaller chrono units with zero.

With the example above, the new attributes of entries are shown below:

```JSON
[
    {
        "id": 1,
        "createdAt" : "2024-04-14T17:00:00.000+00:00",
        "day": "2024-04-15T00:00:000+07:00"
    },
    {
        "id": 2,
        "createdAt" : "2024-04-15T16:30:00.000+00:00",
        "day": "2024-04-15T00:00:000+07:00"
    },
    {
        "id": 3,
        "createdAt" : "2024-04-17T19:25:00.000+00:00",
        "day": "2024-04-18T00:00:000+07:00"
    },
    {
        "id": 4,
        "createdAt" : "2024-04-18T00:30:00.000+00:00",
        "day": "2024-04-18T00:00:000+07:00"
    }
]
```

After group the entries, we have:

```JSON
[
    {
        "count": 2,
        "day": "2024-04-15T00:00:000+07:00"
    },
    {
        "count": 2,
        "day": "2024-04-18T00:00:000+07:00"
    },
]
```

Finally, to add missing interval entries between the start date and the end date, we traverse the grouped entries with the truncated start date and truncated end date.

* With the example above, the start date is 2024-04-15T00:00:000+07:00, and the end date is 2024-04-19T00:00:000+07:00.

So that is how it works!

## Acknowledgements

* This project was inspired by [FusionAuth](https://www.youtube.com/watch?v=IPB8Rig52PI&pp=ygUYbG9naW4gcmVwb3J0IGZ1c2lvbiBhdXRo) and [this tutorial](https://www.youtube.com/watch?v=Nv2DERaMx-4&list=PLhoQRYUPojDQ7yuukL-cn7zZgmjNmy-S1&index=3&pp=gAQBiAQB).

[![CircleCI](https://circleci.com/gh/haluktiras/event-processor.svg?style=svg)](https://app.circleci.com/pipelines/github/haluktiras/event-processor)

## Context ##

In a nutshell, the assignment revolves around reading JSON events from an
`InputStream`, processing then, and writing the result to an `OutputStream`.

The input event stream comprises "pick events"; each of these events represents
the action of a warehouse employee fulfilling a customer order ("picking" an
item off a shelf).
- No assumptions should be made about the source of this event stream. Perhaps
  the events are read from a file, perhaps they arrive over a TCP stream from
  the other side of the world, or perhaps they are auto-generated by a test
  framework ;).
- No assumptions should be made about the speed at which events arrive.
  Multiple events may arrive in brief succession, but it could also be that no
  events arrive for extended periods of time.
- Each event adheres to the JSON format described below.
- Each event comprises a single line of JSON.
- Events are separated by a newline ('\n').
- If no events are sent for a while, `keep-alive` messages consisting of a
  single `\n` may be sent.

### Pick event type specification

|  Field      | Type    | Description                                                                                        |
|:-----------:|:-------:|:--------------------------------------------------------------------------------------------------:|
| `id`        | String  | A unique identifier                                                                                |
| `timestamp` | String  | The time at which the event was emitted; formatted as an [ISO 8601][iso-8601] UTC date-time string |
| `picker`    | Object  | Picker object, see below                                                                           |
| `article`   | Object  | Article object, see below                                                                          |
| `quantity`  | Integer | The number of articles picked                                                                      |

### Picker type specification

| Field           | Type   | Description                                                                                                |
|:---------------:|:------:|:----------------------------------------------------------------------------------------------------------:|
| `id`            | String | A unique identifier                                                                                        |
| `name`          | String | The person's name                                                                                          |
| `active_since`  | String | The time the picker clocked in to start working; formatted as an [ISO 8601][iso-8601] UTC date-time string |

### Article type specification

| Field              | Type   | Description                     |
|:------------------:|:------:|:-------------------------------:|
| `id`               | String | A unique identifier             |
| `name`             | String | An English, human-readable name |
| `temperature_zone` | String | Either `ambient` or `chilled`   |

### Example input event JSON representation

The following JSON object is an example of the kind of event that may be
received from the `InputStream`. Note how it matches the specification above.
There is one difference: you may assume that the events read from the
`InputStream` comprise a single line. (I.e., they are not formatted.)

```json
{
  "timestamp": "2018-12-20T11:50:48Z",
  "id": "2344",
  "picker": {
    "id": "14",
    "name": "Joris",
    "active_since": "2018-12-20T08:20:15Z"
  },
  "article": {
    "id": "13473",
    "name": "ACME Bananas",
    "temperature_zone": "ambient"
  },
  "quantity": 2
}
```

## Task ##

* An `EventProcessorFactory` implementation which produces `StreamProcessor`s
  capable of reading the event stream described above.
* The factory must be [service loadable][service-loader]. (The provided
  `PickingEventProcessorFactory` already meets this criterion through use of
  the `@AutoService` annotation.)
* The factory should produce processors that respect the given `maxEvents` and
  `maxTime` parameters. For example,
  `myEventProcessorFactory.createProcessor(100, Duration.ofSeconds(30))` should
  produce a `StreamProcessor` which for every invocation of
  `StreamProcessor#process` returns after reading at most 100 events or after
  30 seconds, whichever condition is met first.
* A processor should process the events it receives as follows:
  - Only pick events corresponding to picks of `ambient` articles are retained.
    (Picks of chilled articles do count towards the `maxEvents` limit but are
    otherwise ignored.)
  - Events must be grouped by `picker`.
  - Pickers must be sorted chronologically (ascending) by their `active_since`
    timestamp, breaking ties by ID.
  - The picks per picker must also be sorted chronologically, ascending. (Note
    that events may not _arrive_ in chronological order!)
  - The article names should be capitalized.
* A processor must write the result of the aforementioned filter, group and
  sort operations to the provided `OutputStream` according the following JSON
  format:
  ```json
  [
    {
      "picker_name": "Joris",
      "active_since": "2018-09-20T08:20:00Z",
      "picks": [
        {
          "article_name": "ACME BANANAS",
          "timestamp": "2018-12-20T11:50:48Z"
        },
        ... more picks here ...
      ]
    },
    ... more pickers here ...
  ]
  ```



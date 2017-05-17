# Experimental Reddit App

I'm using this repository to showcase my proficiency with technologies relevant to the development of Android apps and experiment with new technologies.

The architecture of this app is based on the idea of model-view-intent (MVI) and some concepts from Redux. The basic idea is as follows:

- At each point in time the UI is a function of the state, i.e., `ui = render(state)`
- The state is modelled explicitly as one object containing the necessary data
- As soon as the state is modified the UI is updated
- The view represents the UI and thus must be rendered from the state
- The view exposes a stream (RxJava Observable) of intentions, 
which are basically the inputs from the user: `intentions: Observable<ViewIntention>`
- The intentions are transformed into `Action`s that are understood by the business layer
- These action are processed by the business layer and lead to a modification of the state, 
in particular the business layer produces from the action stream a stream of (intermediate) results: 
`handleActions(actions: Observable<Action>): Observable<Result>`
- A state reducer function is defined that given the old state and a result produces a new state: `newState = reduce(oldState, result)`
- Given an initial state, a Observable of states can be defined as follows: `states = results.scan(initial, reduce)`
- The UI can then be updated by subscribing to this observable: `states.subscribe(view::render)`
- In this example, a `StateRenderer` is used as an intermediary that manually computs the difference 
between consecutive states and updates the view


Notable technologies/libraries used:
- Programming language: [Kotlin](http://kotl.in)
- Navigation: [Conductor](https://github.com/bluelinelabs/Conductor) (controllers play the role of views)
- [RxJava](https://github.com/ReactiveX/RxJava)
- Dependency Injection: [Dagger](https://github.com/google/dagger)
- [Retrofit](https://github.com/square/retrofit)
- [NYTimes Store](https://github.com/NYTimes/Store)
- [Litho](https://github.com/facebook/litho) (experimental)
- [AutoValue](https://github.com/google/auto/tree/master/value)

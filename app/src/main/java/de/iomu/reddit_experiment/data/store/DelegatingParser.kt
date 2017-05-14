package de.iomu.reddit_experiment.data.store

import com.nytimes.android.external.store2.base.Parser
import de.iomu.reddit_experiment.data.model.Link
import de.iomu.reddit_experiment.data.model.Listing
import de.iomu.reddit_experiment.data.model.Thing
import okio.BufferedSource

abstract class DelegatingParser<Raw, Intermediate, Final>(val delegate: Parser<Raw, Intermediate>) : Parser<Raw, Final> {
    override fun apply(raw: Raw): Final {
        return transform(delegate.apply(raw))
    }

    abstract fun transform(intermediate: Intermediate): Final
}

class SubredditListingTransformer(delegate: Parser<BufferedSource, Thing<Listing<Thing<Link>>>>) :
        DelegatingParser<BufferedSource, Thing<Listing<Thing<Link>>>, Listing<Link>>(delegate) {
    override fun transform(intermediate: Thing<Listing<Thing<Link>>>): Listing<Link> {
        val children = intermediate.data().children.map { it.data() }
        return Listing(children, before = intermediate.data().before, after = intermediate.data().after)
    }
}
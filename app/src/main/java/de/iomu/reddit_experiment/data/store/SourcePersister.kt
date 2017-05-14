package de.iomu.reddit_experiment.data.store

import com.nytimes.android.external.fs2.FSReader
import com.nytimes.android.external.fs2.FSWriter
import com.nytimes.android.external.fs2.filesystem.FileSystem
import com.nytimes.android.external.fs2.filesystem.FileSystemFactory
import com.nytimes.android.external.store2.base.Persister
import io.reactivex.Observable
import okio.BufferedSource
import java.io.File

class SourcePersister<K>(fileSystem: FileSystem, resolver: (K) -> String) : Persister<BufferedSource, K> {
    val reader = FSReader<K>(fileSystem, resolver)
    val writer = FSWriter<K>(fileSystem, resolver)

    constructor(root: File, resolver: (K) -> String) : this(FileSystemFactory.create(root), resolver)

    override fun read(key: K): Observable<BufferedSource> {
        return reader.read(key)
    }

    override fun write(key: K, raw: BufferedSource): Observable<Boolean> {
        return writer.write(key, raw)
    }
}
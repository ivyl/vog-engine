package cache.sound

import javax.sound.sampled.AudioFormat

/** Simple audio sample holding class
 *  @param  byteSample sample stored as bytes
 *  @param  format     audio format representation, for playback purposes
 *  @author Ivyl
 */
class SoundSample (val chunks: List[Array[Byte]], val format: AudioFormat, val chunkSize: Int, val lastChunkSize: Int)


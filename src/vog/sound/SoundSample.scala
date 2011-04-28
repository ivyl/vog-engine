package sound

import javax.sound.sampled.AudioFormat

/**
 *  Simple audio sample holding class
 *  @param  chunks        sample stored as list of byte arrays
 *  @param  format        audio format representation, for playback purposes
 *  @param  chunkSize     single chunk size
 *  @param  lastChunkSize size of last chunk (may be not fully filled)
 *  @author Ivyl
 */
class SoundSample (val chunks: List[Array[Byte]], val format: AudioFormat, val chunkSize: Int, val lastChunkSize: Int)


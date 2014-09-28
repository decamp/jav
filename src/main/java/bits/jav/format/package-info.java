/**
 * Libavformat (lavf) is a library for dealing with various media container
 * formats. Its main two purposes are demuxing - i.e. splitting a media file
 * into component streams, and the reverse process of muxing - writing supplied
 * data in a specified container format. It also has an @ref lavf_io
 * "I/O module" which supports a number of protocols for accessing the data (e.g.
 * file, tcp, http and others). Before using lavf, you need to call
 * av_register_all() to register all compiled muxers, demuxers and protocols.
 * Unless you are absolutely sure you won't use libavformat's network
 * capabilities, you should also call avformat_network_init().
 *
 * A supported input format is described by an AVInputFormat struct, conversely
 * an output format is described by AVOutputFormat. You can iterate over all
 * registered input/output formats using the av_iformat_next() /
 * av_oformat_next() functions. The protocols layer is not part of the public
 * API, so you can only get the names of supported protocols with the
 * avio_enum_protocols() function.
 *
 * Main lavf structure used for both muxing and demuxing is AVFormatContext,
 * which exports all information about the file being read or written. As with
 * most Libavformat structures, its size is not part of public ABI, so it cannot be
 * allocated on stack or directly with av_malloc(). To create an
 * AVFormatContext, use avformat_alloc_context() (some functions, like
 * avformat_open_input() might do that for you).
 *
 * Most importantly an AVFormatContext contains:
 * <li>
 * the @ref AVFormatContext.iformat "input" or @ref AVFormatContext.oformat
 * "output" format. It is either autodetected or set by user for input;
 * always set by user for output.
 * </li><li>
 * an @ref AVFormatContext.streams "array" of AVStreams, which describe all
 * elementary streams stored in the file. AVStreams are typically referred to
 * using their index in this array.
 * </li><li>
 * an @ref AVFormatContext.pb "I/O context". It is either opened by lavf or
 * set by user for input, always set by user for output (unless you are dealing
 * with an AVFMT_NOFILE format).
 * </i>
 *
 * <p> Passing options to (de)muxers <br>
 * Lavf allows to configure muxers and demuxers using the @ref avoptions
 * mechanism. Generic (format-independent) libavformat options are provided by
 * AVFormatContext, they can be examined from a user program by calling
 * av_opt_next() / av_opt_find() on an allocated AVFormatContext (or its AVClass
 * from avformat_get_class()). Private (format-specific) options are provided by
 * AVFormatContext.priv_data if and only if AVInputFormat.priv_class /
 * AVOutputFormat.priv_class of the corresponding format struct is non-NULL.
 * Further options may be provided by the @ref AVFormatContext.pb "I/O context",
 * if its AVClass is non-NULL, and the protocols layer. See the discussion on
 * nesting in @ref avoptions documentation to learn how to access those.
 *
 * <p> Demuxing <br>
 * Demuxers read a media file and split it into chunks of data (@em packets). A
 * {@link bits.jav.codec.JavPacket} "packet" contains one or more encoded frames which belongs to a
 * single elementary stream. In the lavf API this process is represented by the
 * avformat_open_input() function for opening a file, av_read_frame() for
 * reading a single packet and finally avformat_close_input(), which does the
 * cleanup.
 *
 * <p>Opening a media file <br>
 * The minimum information required to open a file is its URL or filename, which
 * is passed to avformat_open_input(), as in the following code:
 * {@code
 * const char    *url = "in.mp3";
 * AVFormatContext *s = NULL;
 * int ret = avformat_open_input(&s, url, NULL, NULL);
 * if (ret < 0)
 *     abort();
 * }
 *
 * The above code attempts to allocate an AVFormatContext, open the
 * specified file (autodetecting the format) and read the header, exporting the
 * information stored there into s. Some formats do not have a header or do not
 * store enough information there, so it is recommended that you call the
 * avformat_find_stream_info() function which tries to read and decode a few
 * frames to find missing information.
 *
 * <p>In some cases you might want to preallocate an AVFormatContext yourself with
 * avformat_alloc_context() and do some tweaking on it before passing it to
 * avformat_open_input(). One such case is when you want to use custom functions
 * for reading input data instead of lavf internal I/O layer.
 * To do that, create your own AVIOContext with avio_alloc_context(), passing
 * your reading callbacks to it. Then set the @em pb field of your
 * AVFormatContext to newly created AVIOContext.
 *
 * <p>Since the format of the opened file is in general not known until after
 * avformat_open_input() has returned, it is not possible to set demuxer private
 * options on a preallocated context. Instead, the options should be passed to
 * avformat_open_input() wrapped in an AVDictionary:
 *
 * {@code
 * AVDictionary *options = NULL;
 * av_dict_set(&options, "video_size", "640x480", 0);
 * av_dict_set(&options, "pixel_format", "rgb24", 0);
 *
 * if (avformat_open_input(&s, url, NULL, &options) < 0)
 *     abort();
 * av_dict_free(&options);
 * }
 *
 * This code passes the private options 'video_size' and 'pixel_format' to the
 * demuxer. They would be necessary for e.g. the rawvideo demuxer, since it
 * cannot know how to interpret raw video data otherwise. If the format turns
 * out to be something different than raw video, those options will not be
 * recognized by the demuxer and therefore will not be applied. Such unrecognized
 * options are then returned in the options dictionary (recognized options are
 * consumed). The calling program can handle such unrecognized options as it
 * wishes, e.g.
 *
 * {@code
 * AVDictionaryEntry *e;
 * if (e = av_dict_get(options, "", NULL, AV_DICT_IGNORE_SUFFIX)) {
 *     fprintf(stderr, "Option %s not recognized by the demuxer.\n", e->key);
 *     abort();
 * }
 * }
 *
 * After you have finished reading the file, you must close it with
 * avformat_close_input(). It will free everything associated with the file.
 *
 * <p>Reading from an opened file <br>
 * Reading data from an opened AVFormatContext is done by repeatedly calling
 * av_read_frame() on it. Each call, if successful, will return an AVPacket
 * containing encoded data for one AVStream, identified by
 * AVPacket.stream_index. This packet may be passed straight into the libavcodec
 * decoding functions avcodec_decode_video2(), avcodec_decode_audio4() or
 * avcodec_decode_subtitle2() if the caller wishes to decode the data.
 *
 * AVPacket.pts, AVPacket.dts and AVPacket.duration timing information will be
 * set if known. They may also be unset (i.e. AV_NOPTS_VALUE for
 * pts/dts, 0 for duration) if the stream does not provide them. The timing
 * information will be in AVStream.time_base units, i.e. it has to be
 * multiplied by the timebase to convert them to seconds.
 *
 * If AVPacket.buf is set on the returned packet, then the packet is
 * allocated dynamically and the user may keep it indefinitely.
 * Otherwise, if AVPacket.buf is NULL, the packet data is backed by a
 * static storage somewhere inside the demuxer and the packet is only valid
 * until the next av_read_frame() call or closing the file. If the caller
 * requires a longer lifetime, av_dup_packet() will make an av_malloc()ed copy
 * of it.
 * In both cases, the packet must be freed with av_free_packet() when it is no
 * longer needed.
 *
 * <p>Seeking <br>
 * Good luck.
 *
 * <p>Muxing <br>
 * Muxers take encoded data in the form of @ref AVPacket "AVPackets" and write
 * it into files or other output bytestreams in the specified container format.
 *
 * <p>The main API functions for muxing are avformat_write_header() for writing the
 * file header, av_write_frame() / av_interleaved_write_frame() for writing the
 * packets and av_write_trailer() for finalizing the file.
 *
 * <p>At the beginning of the muxing process, the caller must first call
 * avformat_alloc_context() to create a muxing context. The caller then sets up
 * the muxer by filling the various fields in this context:
 *
 * - The {@link bits.jav.format.JavFormatContext } "oformat" field must be set to select the
 *   muxer that will be used.
 * - Unless the format is of the AVFMT_NOFILE type, the @ref AVFormatContext.pb
 *   "pb" field must be set to an opened IO context, either returned from
 *   avio_open2() or a custom one.
 * - Unless the format is of the AVFMT_NOSTREAMS type, at least one stream must
 *   be created with the avformat_new_stream() function. The caller should fill
 *   the @ref AVStream.codec "stream codec context" information, such as the
 *   codec @ref AVCodecContext.codec_type "type", @ref AVCodecContext.codec_id
 *   "id" and other parameters (e.g. width / height, the pixel or sample format,
 *   etc.) as known. The @ref AVStream.time_base "stream timebase" should
 *   be set to the timebase that the caller desires to use for this stream (note
 *   that the timebase actually used by the muxer can be different, as will be
 *   described later).
 * - The caller may fill in additional information, such as @ref
 *   AVFormatContext.metadata "global" or @ref AVStream.metadata "per-stream"
 *   metadata, @ref AVFormatContext.chapters "chapters", @ref
 *   AVFormatContext.programs "programs", etc. as described in the
 *   AVFormatContext documentation. Whether such information will actually be
 *   stored in the output depends on what the container format and the muxer
 *   support.
 *
 * <p>When the muxing context is fully set up, the caller must call
 * avformat_write_header() to initialize the muxer internals and write the file
 * header. Whether anything actually is written to the IO context at this step
 * depends on the muxer, but this function must always be called. Any muxer
 * private options must be passed in the options parameter to this function.
 *
 * <p>The data is then sent to the muxer by repeatedly calling av_write_frame() or
 * av_interleaved_write_frame() (consult those functions' documentation for
 * discussion on the difference between them; only one of them may be used with
 * a single muxing context, they should not be mixed). Do note that the timing
 * information on the packets sent to the muxer must be in the corresponding
 * AVStream's timebase. That timebase is set by the muxer (in the
 * avformat_write_header() step) and may be different from the timebase
 * requested by the caller.
 *
 * <p>Once all the data has been written, the caller must call av_write_trailer()
 * to flush any buffered packets and finalize the output file, then close the IO
 * context (if any) and finally free the muxing context with
 * avformat_free_context().
 */

package bits.jav.format;
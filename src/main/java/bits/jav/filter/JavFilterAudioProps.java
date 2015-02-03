//package bits.jav.filter;
//
//import bits.jav.util.*;
//
//
///**
// * Audio specific properties in a reference to an AVFilterBuffer. Since
// * AVFilterBufferRef is common to different media formats, audio specific
// * per reference properties must be separated out.
// */
//public class JavFilterAudioProps implements NativeObject {
//
//
//    public static JavFilterAudioProps alloc() {
//        long p = nAlloc();
//        return p == 0 ? null : new JavFilterAudioProps( p, ReleaseMethod.SELF );
//    }
//
//
//    private long mPointer;
//    private ReleaseMethod mRelease;
//
//
//    JavFilterAudioProps( long pointer, ReleaseMethod release ) {
//        mPointer = pointer;
//        mRelease = release;
//    }
//
//
//
//    public long channelLayout() {
//        return nChannelLayout( mPointer );
//    }
//
//
//    public void channelLayout( long layout ) {
//        nChannelLayout( mPointer, layout );
//    }
//
//
//    public int nbSamples() {
//        return nNbSamples( mPointer );
//    }
//
//
//    public void nbSamples( int nbSamples ) {
//        nNbSamples( mPointer, nbSamples );
//    }
//
//
//    public int sampleRate() {
//        return nSampleRate( mPointer );
//    }
//
//
//    public void sampleRate( int sampleRate ) {
//        nSampleRate( mPointer, sampleRate );
//    }
//
//
//    public int channels() {
//        return nChannels( mPointer );
//    }
//
//
//    public void channels( int channels ) {
//        nChannels( mPointer, channels );
//    }
//
//
//
//    @Override
//    public long pointer() {
//        return 0;
//    }
//
//    @Override
//    public ReleaseMethod releaseMethod() {
//        return mRelease;
//    }
//
//
//    public void releaseMethod( ReleaseMethod release ) {
//        mRelease = release;
//    }
//
//
//    public void release() {
//        long p = mPointer;
//        mPointer = 0;
//        if( mRelease != ReleaseMethod.OWNER && p != 0L ) {
//            nFree( p );
//        }
//    }
//
//    @Override
//    protected void finalize() throws Throwable {
//        release();
//        super.finalize();
//    }
//
//    private static native long nAlloc();
//    private static native void nFree( long pointer );
//
//    private static native long nChannelLayout( long pointer );
//    private static native void nChannelLayout( long pointer, long layout );
//    private static native int  nNbSamples( long pointer );
//    private static native void nNbSamples( long pointer, int nbSamples );
//    private static native int  nSampleRate( long pointer );
//    private static native void nSampleRate( long pointer, int sampleRate );
//    private static native int  nChannels( long pointer );
//    private static native void nChannels( long pointer, int channels );
//
//}
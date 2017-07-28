// INyaaNyaaMusicService.aidl
package xyz.lostalishar.nyaanyaamusicplayer.service;

import xyz.lostalishar.nyaanyaamusicplayer.model.MusicPlaybackState;
import xyz.lostalishar.nyaanyaamusicplayer.model.MusicPlaybackTrack;

interface INyaaNyaaMusicService {
    boolean load(int queuePos);
    void play();
    void pause();
    void stop();
    void reset();
    void next();
    void previous();
    MusicPlaybackState getState();
    List<MusicPlaybackTrack> getQueue();
    int enqueue(in long[] musicIdList, out int[] addedList);
    int dequeue(in long[] musicIdList, out long[] removedList);
    boolean isPlaying();
}



# AudioTrack

# MediaCodec


# FFmpeg

ffmpeg  -i  /Users/liyang/c/data/mp4/in.mp4 -s 1920x1080  -b:v 1M  -r 20 /Users/liyang/c/data/mp4/in_ffmpeg.mp4

```json
{
    "streams": [
        {
            "index": 0,
            "codec_name": "h264",
            "codec_long_name": "H.264 / AVC / MPEG-4 AVC / MPEG-4 part 10",
            "profile": "High",
            "codec_type": "video",
            "codec_tag_string": "avc1",
            "codec_tag": "0x31637661",
            "width": 624,
            "height": 368,
            "coded_width": 624,
            "coded_height": 368,
            "closed_captions": 0,
            "film_grain": 0,
            "has_b_frames": 1,
            "pix_fmt": "yuv420p",
            "level": 30,
            "color_range": "tv",
            "color_space": "bt709",
            "color_transfer": "bt709",
            "color_primaries": "bt709",
            "chroma_location": "left",
            "field_order": "progressive",
            "refs": 1,
            "is_avc": "true",
            "nal_length_size": "4",
            "id": "0x1",
            "r_frame_rate": "20/1",
            "avg_frame_rate": "564600/28243",
            "time_base": "1/600",
            "start_pts": 0,
            "start_time": "0.000000",
            "duration_ts": 84699,
            "duration": "141.165000",
            "bit_rate": "292679",
            "bits_per_raw_sample": "8",
            "nb_frames": "2823",
            "extradata_size": 34,
            "disposition": {
                "default": 1,
                "dub": 0,
                "original": 0,
                "comment": 0,
                "lyrics": 0,
                "karaoke": 0,
                "forced": 0,
                "hearing_impaired": 0,
                "visual_impaired": 0,
                "clean_effects": 0,
                "attached_pic": 0,
                "timed_thumbnails": 0,
                "captions": 0,
                "descriptions": 0,
                "metadata": 0,
                "dependent": 0,
                "still_image": 0
            },
            "tags": {
                "creation_time": "2020-04-30T04:00:47.000000Z",
                "language": "und",
                "handler_name": "Core Media Video",
                "vendor_id": "[0][0][0][0]"
            }
        },
        {
            "index": 1,
            "codec_name": "aac",
            "codec_long_name": "AAC (Advanced Audio Coding)",
            "profile": "LC",
            "codec_type": "audio",
            "codec_tag_string": "mp4a",
            "codec_tag": "0x6134706d",
            "sample_fmt": "fltp",
            "sample_rate": "44100",
            "channels": 1,
            "channel_layout": "mono",
            "bits_per_sample": 0,
            "id": "0x2",
            "r_frame_rate": "0/0",
            "avg_frame_rate": "0/0",
            "time_base": "1/44100",
            "start_pts": 0,
            "start_time": "0.000000",
            "duration_ts": 6217664,
            "duration": "140.990113",
            "bit_rate": "47560",
            "nb_frames": "6073",
            "extradata_size": 2,
            "disposition": {
                "default": 1,
                "dub": 0,
                "original": 0,
                "comment": 0,
                "lyrics": 0,
                "karaoke": 0,
                "forced": 0,
                "hearing_impaired": 0,
                "visual_impaired": 0,
                "clean_effects": 0,
                "attached_pic": 0,
                "timed_thumbnails": 0,
                "captions": 0,
                "descriptions": 0,
                "metadata": 0,
                "dependent": 0,
                "still_image": 0
            },
            "tags": {
                "creation_time": "2020-04-30T04:00:47.000000Z",
                "language": "und",
                "handler_name": "Core Media Audio",
                "vendor_id": "[0][0][0][0]"
            }
        }
    ],
    "chapters": [

    ],
    "format": {
        "filename": "C:\\Users\\liyang\\Desktop\\鏉庨槼涓撶敤娴嬭瘯鏁版嵁\\瑙嗛\\in.mp4",
        "nb_streams": 2,
        "nb_programs": 0,
        "format_name": "mov,mp4,m4a,3gp,3g2,mj2",
        "format_long_name": "QuickTime / MOV",
        "start_time": "0.000000",
        "duration": "141.165011",
        "size": "6071657",
        "bit_rate": "344088",
        "probe_score": 100,
        "tags": {
            "major_brand": "mp42",
            "minor_version": "1",
            "compatible_brands": "isommp41mp42",
            "creation_time": "2020-04-30T04:00:47.000000Z"
        }
    }
}
```


# OpenSL ES
[https://zhuanlan.zhihu.com/p/451251100]

#OpenGL ES
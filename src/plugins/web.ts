import { WebPlugin } from '@capacitor/core';
import { PhotoGalleryPlugin, MediumType } from './definitions';

export class PhotoGalleryWeb extends WebPlugin implements PhotoGalleryPlugin {
    async listAlbums(opts: { mediumType: MediumType }) {

        const data = [
            {
                "id": "__ALL__",
                "mediumType": "image",
                "name": "All",
                "count": 471
            },
            {
                "id": "155204025",
                "mediumType": "image",
                "name": "BOGO Fieldworks",
                "count": 53
            },
            {
                "id": "-2075869745",
                "mediumType": "image",
                "name": "bogo",
                "count": 1
            },
            {
                "id": "-869026771",
                "mediumType": "image",
                "name": "daum_cafe",
                "count": 2
            },
            {
                "id": "-1739773001",
                "mediumType": "image",
                "name": "Camera",
                "count": 106
            },
            {
                "id": "-1313584517",
                "mediumType": "image",
                "name": "Screenshots",
                "count": 40
            },
            {
                "id": "540528482",
                "mediumType": "image",
                "name": "Download",
                "count": 22
            },
            {
                "id": "-1617409521",
                "mediumType": "image",
                "name": "Pictures",
                "count": 1
            },
            {
                "id": "-853169169",
                "mediumType": "image",
                "name": "KakaoTalk",
                "count": 141
            },
            {
                "id": "1581398904",
                "mediumType": "image",
                "name": "Photoshop Express",
                "count": 1
            },
            {
                "id": "-1188092050",
                "mediumType": "image",
                "name": "Samsung Flow",
                "count": 7
            },
            {
                "id": "-481230434",
                "mediumType": "image",
                "name": "Testpic",
                "count": 1
            },
            {
                "id": "-593691459",
                "mediumType": "image",
                "name": "보고웍스",
                "count": 96
            }
        ];

        return {
            data : JSON.stringify(data)
        };
    }

    async listMedia(opts: {
        albumId : string,
        mediumType : MediumType,
        newest : boolean,
        total : number,
        skip : number,
        take : number,
    }) { 

        const data = {
            "newest": true,
            "start": 0,
            "total": 96,
            "items": [
                {
                    "id": "23071",
                    "mediumType": "image",
                    "width": 504,
                    "height": 1080,
                    "mimeType": "image/png",
                    "creationDate": 1619680850000,
                    "modifiedDate": 1619680850000
                },
                {
                    "id": "23072",
                    "mediumType": "image",
                    "width": 503,
                    "height": 1080,
                    "mimeType": "image/png",
                    "creationDate": 1619680853000,
                    "modifiedDate": 1619680853000
                },
                {
                    "id": "23073",
                    "mediumType": "image",
                    "width": 1080,
                    "height": 1080,
                    "mimeType": "image/png",
                    "creationDate": 1619680895000,
                    "modifiedDate": 1619680895000
                },
                {
                    "id": "23074",
                    "mediumType": "image",
                    "width": 1080,
                    "height": 1080,
                    "mimeType": "image/png",
                    "creationDate": 1619680900000,
                    "modifiedDate": 1619680900000
                },
                {
                    "id": "23075",
                    "mediumType": "image",
                    "width": 504,
                    "height": 1080,
                    "mimeType": "image/png",
                    "creationDate": 1619681009000,
                    "modifiedDate": 1619681009000
                },
                {
                    "id": "23076",
                    "mediumType": "image",
                    "width": 503,
                    "height": 1080,
                    "mimeType": "image/png",
                    "creationDate": 1619681012000,
                    "modifiedDate": 1619681012000
                },
                {
                    "id": "23077",
                    "mediumType": "image",
                    "width": 504,
                    "height": 1080,
                    "mimeType": "image/png",
                    "creationDate": 1619681039000,
                    "modifiedDate": 1619681039000
                },
                {
                    "id": "23078",
                    "mediumType": "image",
                    "width": 503,
                    "height": 1080,
                    "mimeType": "image/png",
                    "creationDate": 1619681042000,
                    "modifiedDate": 1619681042000
                },
                {
                    "id": "23079",
                    "mediumType": "image",
                    "width": 504,
                    "height": 1080,
                    "mimeType": "image/png",
                    "creationDate": 1619681436000,
                    "modifiedDate": 1619681436000
                },
                {
                    "id": "23080",
                    "mediumType": "image",
                    "width": 503,
                    "height": 1080,
                    "mimeType": "image/png",
                    "creationDate": 1619681439000,
                    "modifiedDate": 1619681439000
                },
                {
                    "id": "23081",
                    "mediumType": "image",
                    "width": 1080,
                    "height": 1080,
                    "mimeType": "image/png",
                    "creationDate": 1619763996000,
                    "modifiedDate": 1619763996000
                },
                {
                    "id": "23082",
                    "mediumType": "image",
                    "width": 1080,
                    "height": 1080,
                    "mimeType": "image/png",
                    "creationDate": 1619764001000,
                    "modifiedDate": 1619764001000
                },
                {
                    "id": "23083",
                    "mediumType": "image",
                    "width": 1080,
                    "height": 1080,
                    "mimeType": "image/png",
                    "creationDate": 1619764211000,
                    "modifiedDate": 1619764211000
                },
                {
                    "id": "23084",
                    "mediumType": "image",
                    "width": 1080,
                    "height": 1080,
                    "mimeType": "image/png",
                    "creationDate": 1619764216000,
                    "modifiedDate": 1619764216000
                },
                {
                    "id": "23085",
                    "mediumType": "image",
                    "width": 1080,
                    "height": 1080,
                    "mimeType": "image/png",
                    "creationDate": 1619764564000,
                    "modifiedDate": 1619764564000
                },
                {
                    "id": "23086",
                    "mediumType": "image",
                    "width": 1080,
                    "height": 1080,
                    "mimeType": "image/png",
                    "creationDate": 1619764569000,
                    "modifiedDate": 1619764569000
                },
                {
                    "id": "23091",
                    "mediumType": "image",
                    "width": 1080,
                    "height": 1080,
                    "mimeType": "image/png",
                    "creationDate": 1620970523000,
                    "modifiedDate": 1620970523000
                },
                {
                    "id": "23092",
                    "mediumType": "image",
                    "width": 1080,
                    "height": 1080,
                    "mimeType": "image/png",
                    "creationDate": 1620970929000,
                    "modifiedDate": 1620970929000
                },
                {
                    "id": "23093",
                    "mediumType": "image",
                    "width": 1080,
                    "height": 1080,
                    "mimeType": "image/png",
                    "creationDate": 1620986419000,
                    "modifiedDate": 1620986419000
                },
                {
                    "id": "23094",
                    "mediumType": "image",
                    "width": 1080,
                    "height": 1080,
                    "mimeType": "image/png",
                    "creationDate": 1620986450000,
                    "modifiedDate": 1620986450000
                },
                {
                    "id": "23095",
                    "mediumType": "image",
                    "width": 1080,
                    "height": 1080,
                    "mimeType": "image/png",
                    "creationDate": 1620986511000,
                    "modifiedDate": 1620986511000
                },
                {
                    "id": "23096",
                    "mediumType": "image",
                    "width": 1080,
                    "height": 1080,
                    "mimeType": "image/png",
                    "creationDate": 1620986523000,
                    "modifiedDate": 1620986523000
                },
                {
                    "id": "23097",
                    "mediumType": "image",
                    "width": 125,
                    "height": 75,
                    "mimeType": "image/png",
                    "creationDate": 1620986668000,
                    "modifiedDate": 1620986668000
                },
                {
                    "id": "23098",
                    "mediumType": "image",
                    "width": 1080,
                    "height": 1080,
                    "mimeType": "image/png",
                    "creationDate": 1620986676000,
                    "modifiedDate": 1620986676000
                },
                {
                    "id": "23100",
                    "mediumType": "image",
                    "width": 122,
                    "height": 252,
                    "mimeType": "image/png",
                    "creationDate": 1621220195000,
                    "modifiedDate": 1621220195000
                },
                {
                    "id": "23099",
                    "mediumType": "image",
                    "width": 122,
                    "height": 252,
                    "mimeType": "image/png",
                    "creationDate": 1621220195000,
                    "modifiedDate": 1621220195000
                },
                {
                    "id": "23101",
                    "mediumType": "image",
                    "width": 1080,
                    "height": 487,
                    "mimeType": "image/png",
                    "creationDate": 1621220996000,
                    "modifiedDate": 1621220996000
                },
                {
                    "id": "23103",
                    "mediumType": "image",
                    "width": 1080,
                    "height": 1080,
                    "mimeType": "image/png",
                    "creationDate": 1621221029000,
                    "modifiedDate": 1621221029000
                },
                {
                    "id": "23102",
                    "mediumType": "image",
                    "width": 125,
                    "height": 75,
                    "mimeType": "image/png",
                    "creationDate": 1621221029000,
                    "modifiedDate": 1621221029000
                },
                {
                    "id": "23104",
                    "mediumType": "image",
                    "width": 75,
                    "height": 75,
                    "mimeType": "image/png",
                    "creationDate": 1621229954000,
                    "modifiedDate": 1621229954000
                },
                {
                    "id": "23105",
                    "mediumType": "image",
                    "width": 612,
                    "height": 612,
                    "mimeType": "image/png",
                    "creationDate": 1621230018000,
                    "modifiedDate": 1621230018000
                },
                {
                    "id": "23106",
                    "mediumType": "image",
                    "width": 75,
                    "height": 75,
                    "mimeType": "image/png",
                    "creationDate": 1621230055000,
                    "modifiedDate": 1621230055000
                },
                {
                    "id": "23107",
                    "mediumType": "image",
                    "width": 75,
                    "height": 75,
                    "mimeType": "image/png",
                    "creationDate": 1621230078000,
                    "modifiedDate": 1621230078000
                },
                {
                    "id": "23108",
                    "mediumType": "image",
                    "width": 35,
                    "height": 35,
                    "mimeType": "image/png",
                    "creationDate": 1621230188000,
                    "modifiedDate": 1621230188000
                },
                {
                    "id": "23109",
                    "mediumType": "image",
                    "width": 8,
                    "height": 8,
                    "mimeType": "image/png",
                    "creationDate": 1621230257000,
                    "modifiedDate": 1621230257000
                },
                {
                    "id": "23110",
                    "mediumType": "image",
                    "width": 538,
                    "height": 538,
                    "mimeType": "image/png",
                    "creationDate": 1621230287000,
                    "modifiedDate": 1621230287000
                },
                {
                    "id": "23111",
                    "mediumType": "image",
                    "width": 5,
                    "height": 5,
                    "mimeType": "image/png",
                    "creationDate": 1621230488000,
                    "modifiedDate": 1621230488000
                },
                {
                    "id": "23112",
                    "mediumType": "image",
                    "width": 182,
                    "height": 182,
                    "mimeType": "image/png",
                    "creationDate": 1621230938000,
                    "modifiedDate": 1621230938000
                },
                {
                    "id": "23113",
                    "mediumType": "image",
                    "width": 373,
                    "height": 373,
                    "mimeType": "image/png",
                    "creationDate": 1621231004000,
                    "modifiedDate": 1621231004000
                },
                {
                    "id": "23114",
                    "mediumType": "image",
                    "width": 764,
                    "height": 764,
                    "mimeType": "image/png",
                    "creationDate": 1621231173000,
                    "modifiedDate": 1621231173000
                },
                {
                    "id": "23115",
                    "mediumType": "image",
                    "width": 411,
                    "height": 411,
                    "mimeType": "image/png",
                    "creationDate": 1621232195000,
                    "modifiedDate": 1621232195000
                },
                {
                    "id": "23116",
                    "mediumType": "image",
                    "width": 413,
                    "height": 413,
                    "mimeType": "image/png",
                    "creationDate": 1621232211000,
                    "modifiedDate": 1621232211000
                },
                {
                    "id": "23117",
                    "mediumType": "image",
                    "width": 133,
                    "height": 133,
                    "mimeType": "image/png",
                    "creationDate": 1621232357000,
                    "modifiedDate": 1621232357000
                },
                {
                    "id": "23118",
                    "mediumType": "image",
                    "width": 504,
                    "height": 504,
                    "mimeType": "image/png",
                    "creationDate": 1621232387000,
                    "modifiedDate": 1621232387000
                },
                {
                    "id": "23119",
                    "mediumType": "image",
                    "width": 413,
                    "height": 413,
                    "mimeType": "image/png",
                    "creationDate": 1621232410000,
                    "modifiedDate": 1621232410000
                },
                {
                    "id": "23120",
                    "mediumType": "image",
                    "width": 373,
                    "height": 373,
                    "mimeType": "image/png",
                    "creationDate": 1621232411000,
                    "modifiedDate": 1621232411000
                },
                {
                    "id": "23122",
                    "mediumType": "image",
                    "width": 8,
                    "height": 8,
                    "mimeType": "image/png",
                    "creationDate": 1621232420000,
                    "modifiedDate": 1621232420000
                },
                {
                    "id": "23121",
                    "mediumType": "image",
                    "width": 538,
                    "height": 538,
                    "mimeType": "image/png",
                    "creationDate": 1621232420000,
                    "modifiedDate": 1621232420000
                },
                {
                    "id": "23136",
                    "mediumType": "image",
                    "width": 1080,
                    "height": 1080,
                    "mimeType": "image/png",
                    "creationDate": 1621820791000,
                    "modifiedDate": 1621820791000
                },
                {
                    "id": "23137",
                    "mediumType": "image",
                    "width": 504,
                    "height": 1080,
                    "mimeType": "image/png",
                    "creationDate": 1621820986000,
                    "modifiedDate": 1621820986000
                },
                {
                    "id": "23138",
                    "mediumType": "image",
                    "width": 1080,
                    "height": 1080,
                    "mimeType": "image/png",
                    "creationDate": 1621821267000,
                    "modifiedDate": 1621821267000
                },
                {
                    "id": "23139",
                    "mediumType": "image",
                    "width": 504,
                    "height": 1080,
                    "mimeType": "image/png",
                    "creationDate": 1621821273000,
                    "modifiedDate": 1621821273000
                },
                {
                    "id": "23140",
                    "mediumType": "image",
                    "width": 125,
                    "height": 75,
                    "mimeType": "image/png",
                    "creationDate": 1621821390000,
                    "modifiedDate": 1621821390000
                },
                {
                    "id": "23141",
                    "mediumType": "image",
                    "width": 1080,
                    "height": 1080,
                    "mimeType": "image/png",
                    "creationDate": 1622439589000,
                    "modifiedDate": 1622439589000
                },
                {
                    "id": "23142",
                    "mediumType": "image",
                    "width": 1080,
                    "height": 1080,
                    "mimeType": "image/png",
                    "creationDate": 1622439592000,
                    "modifiedDate": 1622439592000
                },
                {
                    "id": "23143",
                    "mediumType": "image",
                    "width": 1080,
                    "height": 1080,
                    "mimeType": "image/png",
                    "creationDate": 1622439595000,
                    "modifiedDate": 1622439595000
                },
                {
                    "id": "23144",
                    "mediumType": "image",
                    "width": 1080,
                    "height": 1197,
                    "mimeType": "image/jpeg",
                    "creationDate": 1622439744000,
                    "modifiedDate": 1622439744000
                },
                {
                    "id": "23145",
                    "mediumType": "image",
                    "width": 1080,
                    "height": 1080,
                    "mimeType": "image/png",
                    "creationDate": 1622612101000,
                    "modifiedDate": 1622612101000
                },
                {
                    "id": "23146",
                    "mediumType": "image",
                    "width": 1080,
                    "height": 1080,
                    "mimeType": "image/png",
                    "creationDate": 1622615231000,
                    "modifiedDate": 1622615231000
                },
                {
                    "id": "23147",
                    "mediumType": "image",
                    "width": 1080,
                    "height": 1080,
                    "mimeType": "image/png",
                    "creationDate": 1622616146000,
                    "modifiedDate": 1622616146000
                },
                {
                    "id": "23148",
                    "mediumType": "image",
                    "width": 1080,
                    "height": 1080,
                    "mimeType": "image/png",
                    "creationDate": 1622619773000,
                    "modifiedDate": 1622619773000
                },
                {
                    "id": "23170",
                    "mediumType": "image",
                    "width": 1080,
                    "height": 488,
                    "mimeType": "image/png",
                    "creationDate": 1622803143000,
                    "modifiedDate": 1622803143000
                },
                {
                    "id": "23171",
                    "mediumType": "image",
                    "width": 1080,
                    "height": 50,
                    "mimeType": "image/png",
                    "creationDate": 1622803154000,
                    "modifiedDate": 1622803154000
                },
                {
                    "id": "23172",
                    "mediumType": "image",
                    "width": 290,
                    "height": 1080,
                    "mimeType": "image/png",
                    "creationDate": 1622803166000,
                    "modifiedDate": 1622803166000
                },
                {
                    "id": "23173",
                    "mediumType": "image",
                    "width": 325,
                    "height": 1080,
                    "mimeType": "image/png",
                    "creationDate": 1622803182000,
                    "modifiedDate": 1622803182000
                },
                {
                    "id": "23174",
                    "mediumType": "image",
                    "width": 457,
                    "height": 1080,
                    "mimeType": "image/png",
                    "creationDate": 1623048183000,
                    "modifiedDate": 1623048183000
                },
                {
                    "id": "23175",
                    "mediumType": "image",
                    "width": 1080,
                    "height": 1080,
                    "mimeType": "image/png",
                    "creationDate": 1623054370000,
                    "modifiedDate": 1623054370000
                },
                {
                    "id": "23177",
                    "mediumType": "image",
                    "width": 486,
                    "height": 1080,
                    "mimeType": "image/png",
                    "creationDate": 1623116918000,
                    "modifiedDate": 1623116918000
                },
                {
                    "id": "23180",
                    "mediumType": "image",
                    "width": 429,
                    "height": 1600,
                    "mimeType": "image/png",
                    "creationDate": 1623826945000,
                    "modifiedDate": 1623826945000
                },
                {
                    "id": "23181",
                    "mediumType": "image",
                    "width": 481,
                    "height": 1600,
                    "mimeType": "image/png",
                    "creationDate": 1623827001000,
                    "modifiedDate": 1623827001000
                },
                {
                    "id": "23182",
                    "mediumType": "image",
                    "width": 429,
                    "height": 1600,
                    "mimeType": "image/png",
                    "creationDate": 1623827114000,
                    "modifiedDate": 1623827114000
                },
                {
                    "id": "23183",
                    "mediumType": "image",
                    "width": 429,
                    "height": 1600,
                    "mimeType": "image/png",
                    "creationDate": 1623827256000,
                    "modifiedDate": 1623827256000
                },
                {
                    "id": "23184",
                    "mediumType": "image",
                    "width": 1080,
                    "height": 50,
                    "mimeType": "image/png",
                    "creationDate": 1623827266000,
                    "modifiedDate": 1623827266000
                },
                {
                    "id": "23185",
                    "mediumType": "image",
                    "width": 429,
                    "height": 1600,
                    "mimeType": "image/png",
                    "creationDate": 1623827445000,
                    "modifiedDate": 1623827445000
                },
                {
                    "id": "23186",
                    "mediumType": "image",
                    "width": 429,
                    "height": 1600,
                    "mimeType": "image/png",
                    "creationDate": 1623827520000,
                    "modifiedDate": 1623827520000
                },
                {
                    "id": "23207",
                    "mediumType": "image",
                    "width": 106,
                    "height": 55,
                    "mimeType": "image/png",
                    "creationDate": 1625737757000,
                    "modifiedDate": 1625737757000
                },
                {
                    "id": "23225",
                    "mediumType": "image",
                    "width": 240,
                    "height": 240,
                    "mimeType": "image/png",
                    "creationDate": 1627450911000,
                    "modifiedDate": 1627450911000
                },
                {
                    "id": "23263",
                    "mediumType": "image",
                    "width": 1080,
                    "height": 608,
                    "mimeType": "image/png",
                    "creationDate": 1629360178000,
                    "modifiedDate": 1629360178000
                },
                {
                    "id": "23264",
                    "mediumType": "image",
                    "width": 1080,
                    "height": 608,
                    "mimeType": "image/png",
                    "creationDate": 1629360412000,
                    "modifiedDate": 1629360412000
                },
                {
                    "id": "23265",
                    "mediumType": "image",
                    "width": 1080,
                    "height": 608,
                    "mimeType": "image/png",
                    "creationDate": 1629360422000,
                    "modifiedDate": 1629360422000
                },
                {
                    "id": "23278",
                    "mediumType": "image",
                    "width": 1080,
                    "height": 608,
                    "mimeType": "image/png",
                    "creationDate": 1629955660000,
                    "modifiedDate": 1629955660000
                },
                {
                    "id": "23279",
                    "mediumType": "image",
                    "width": 1080,
                    "height": 608,
                    "mimeType": "image/png",
                    "creationDate": 1629956081000,
                    "modifiedDate": 1629956081000
                },
                {
                    "id": "23280",
                    "mediumType": "image",
                    "width": 1080,
                    "height": 608,
                    "mimeType": "image/png",
                    "creationDate": 1629958651000,
                    "modifiedDate": 1629958651000
                },
                {
                    "id": "23281",
                    "mediumType": "image",
                    "width": 1080,
                    "height": 608,
                    "mimeType": "image/png",
                    "creationDate": 1629958672000,
                    "modifiedDate": 1629958672000
                },
                {
                    "id": "23299",
                    "mediumType": "image",
                    "width": 325,
                    "height": 1080,
                    "mimeType": "image/png",
                    "creationDate": 1633397842000,
                    "modifiedDate": 1633397842000
                },
                {
                    "id": "23311",
                    "mediumType": "image",
                    "width": 1080,
                    "height": 607,
                    "mimeType": "image/png",
                    "creationDate": 1634003312000,
                    "modifiedDate": 1634003312000
                },
                {
                    "id": "23321",
                    "mediumType": "image",
                    "width": 1514,
                    "height": 968,
                    "mimeType": "image/png",
                    "creationDate": 1635140578000,
                    "modifiedDate": 1635140578000
                },
                {
                    "id": "23322",
                    "mediumType": "image",
                    "width": 1080,
                    "height": 884,
                    "mimeType": "image/png",
                    "creationDate": 1635140892000,
                    "modifiedDate": 1635140892000
                },
                {
                    "id": "23323",
                    "mediumType": "image",
                    "width": 1080,
                    "height": 270,
                    "mimeType": "image/png",
                    "creationDate": 1635150804000,
                    "modifiedDate": 1635150804000
                },
                {
                    "id": "23324",
                    "mediumType": "image",
                    "width": 1080,
                    "height": 270,
                    "mimeType": "image/png",
                    "creationDate": 1635153447000,
                    "modifiedDate": 1635153447000
                },
                {
                    "id": "23325",
                    "mediumType": "image",
                    "width": 1080,
                    "height": 270,
                    "mimeType": "image/png",
                    "creationDate": 1635153499000,
                    "modifiedDate": 1635153499000
                },
                {
                    "id": "23326",
                    "mediumType": "image",
                    "width": 1080,
                    "height": 884,
                    "mimeType": "image/png",
                    "creationDate": 1635153846000,
                    "modifiedDate": 1635153846000
                },
                {
                    "id": "23327",
                    "mediumType": "image",
                    "width": 1080,
                    "height": 270,
                    "mimeType": "image/png",
                    "creationDate": 1635154290000,
                    "modifiedDate": 1635154290000
                },
                {
                    "id": "23328",
                    "mediumType": "image",
                    "width": 1080,
                    "height": 270,
                    "mimeType": "image/png",
                    "creationDate": 1635154820000,
                    "modifiedDate": 1635154820000
                },
                {
                    "id": "23329",
                    "mediumType": "image",
                    "width": 1080,
                    "height": 270,
                    "mimeType": "image/png",
                    "creationDate": 1635154907000,
                    "modifiedDate": 1635154907000
                },
                {
                    "id": "23393",
                    "mediumType": "image",
                    "width": 0,
                    "height": 0,
                    "mimeType": "image/png",
                    "creationDate": 1648022634000,
                    "modifiedDate": 1648022634000
                }
            ]
        }

        return {
            data : JSON.stringify(data)
        };
    }

    async getMedium(opts: {
        mediumId : string,
        mediumType : MediumType,
        width : number,
        height : number,
        highQuality : boolean
    }) { return { data : '' } }

    async getAlbumThumbnail(opts:{
        albumId : string,
        mediumType : MediumType,
        width : number,
        height : number,
        highQuality : boolean
    }) { return { data : '' } }

    async getFile(opts: {
        mediumId : string,
        mediumType : string,
        mimeType : string
    }) { return { data : '' } }
    
    async cleanCache() {
        return;
    }

    async test() {
        return
    }

    
    async getThumbnail(opts : {
        mediumId : string,
        mediumType : string,
        width : number,
        height : number,
        highQuality : boolean
    }) {
        return { data : '' }
    }
  }

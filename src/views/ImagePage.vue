<template>
    <ion-page>
        <ion-header :translucent="true">
        <ion-toolbar>
            <ion-buttons slot="start">
                <ion-back-button default-href="/"></ion-back-button>
            </ion-buttons>
            <ion-title>Image</ion-title>
        </ion-toolbar>
        </ion-header>
        <ion-content :fullscreen="true">
            <div style="display: flex">
                <ion-button @click="onClickCut" >Cut</ion-button>
            </div>
            <div style="height: 100px" >
                <img style="height: 100%" :src="dstImage" />
            </div>
            <div style="width: 100%; height:100%; position: relative">
                <!-- <PinchZoomVue> -->
                <canvas ref="canvas" style="position: absolute; width: 100%" >
                    <!-- @mousedown="onMouseDown" @mousemove="onMouseMove" @mouseup="onMouseUp" @mousewheel="onMouseWheel" 
                    @mouseleave="onMouseLeave" @mouseover="onMouseOver" @mouseout="onMouseOut">                     -->
                </canvas>
                <!-- </PinchZoomVue> -->
                <canvas ref="crop_canvas" style="position: absolute; width: 100%; touch-action: none"
                    @pointerdown="onPointerDown"
                    @pointerup="onPointerUp"
                    @pointermove="onPointerMove"
                    
                    @mousedown="onMouseDown" @mousemove="onMouseMove" @mouseup="onMouseUp" @mousewheel="onMouseWheel" 
                    @mouseleave="onMouseLeave" @mouseover="onMouseOver" @mouseout="onMouseOut">

                </canvas>
            </div>
            
        </ion-content>
    </ion-page>
</template>

<script>
import { IonButtons, IonButton, IonContent, IonHeader, IonBackButton, IonPage, IonTitle, IonToolbar } from '@ionic/vue';
import { PhotoGallery } from '@/plugins/index'
import { PhotoLibrary } from '@ionic-native/photo-library';
import panzoom from 'panzoom'

import { data } from '@/test/data.js'

const gripSize = 80

class Pt {
    constructor(x, y) {
        this.x = x ?? 0
        this.y = y ?? 0
    }
}

class Rect {
    constructor(x, y, w, h) {
        this.x = x ?? 0
        this.y = y ?? 0
        this.width = w ?? 0
        this.height = h ?? 0
    }
}

class ActionType {
    static l = 1
    static t = 2

    static r = 4
    static b = 8

    static center = 16
    
    static lt = ActionType.l | ActionType.t
    static rt = ActionType.r | ActionType.t
        
    static lb = ActionType.l | ActionType.b    
    static rb = ActionType.r | ActionType.b    
}

function create(pt, type) {
    return {
        pt : pt,
        type : type
    }
}

function hitTest(pt, mousePt) {
    return pt.x - gripSize <= mousePt.x &&
        pt.y - gripSize <= mousePt.y &&
        pt.x + gripSize >= mousePt.x &&
        pt.y + gripSize >= mousePt.y
}

export default {
    components: {
        // PinchZoomVue,
        IonButtons,
        IonContent,
        IonHeader,
        IonPage,
        IonTitle,
        IonToolbar,
        IonBackButton,
        IonButton
    },
    data() {
        return {
            panzoom : null,

            image : null,
            data : null,
            src : null,
            img : null,

            size : null,

            srcCropRect : new Rect(),
            cropRect : new Rect(),

            isPressed : false,
            hit : null,

            dstImage : null,

            capturePointId : null,
            pointerIds : new Set()
        }
    },
    mounted() {       
        console.log('mounted')
        const json = this.$route.query.json

        if(json) {
            this.image = JSON.parse(json)
            this.init()
            return
        } 

        const item = this.$route.query.item

        if(item) {
            const data = JSON.parse(item)            

            PhotoLibrary.getPhoto(data.id).then(async ret => {
                const buf = await ret.arrayBuffer()

                if(buf && buf.byteLength) {
                    this.src = 'data:image;base64,' + btoa(this.arrayBufferToBase64(buf))                    
                    //const temp = btoa(String.fromCharCode.apply(null, new Uint8Array(buf)))

                    return
                }
            })
            return
        }

        const canvas = this.$refs.canvas

        

        let ctx = canvas.getContext('2d')

        let img = new Image()
        img.onload = function(event) {
            
            this.size = {
                width : event.target.naturalWidth,
                height : event.target.naturalHeight
            }

            this.cropRect.x = 50;
            this.cropRect.y = 50;

            const crop_canvas = this.$refs.crop_canvas

            crop_canvas.width = this.size.width
            crop_canvas.height = this.size.height

            this.cropRect.width = canvas.width = this.size.width
            this.cropRect.height = canvas.height = this.size.height

            this.cropRect.width -= 100
            this.cropRect.height -= 100

            this.draw()

            this.panzoom = panzoom(canvas, {
                beforeMouseDown : (e) => {
                    return !this.hit == false
                }
            })
        }.bind(this)
        img.src = 'data:image;base64,' + data
        this.img = img
        this.src = img.src
    },
    unmounted() {
        return
    },
    methods: {
        async init() {
            const ret = await PhotoGallery.getFile({
                mediumId : this.image.id,
                mediumType : this.image.mediumType,
                mimeType : this.image.mimeType
            })

            this.data = ret.data;

            this.src = 'data:' + (this.image.mimeType ?? 'image') + ';base64,'+ ret.byte;
        },
        arrayBufferToBase64( buffer ) {
            var binary = '';
            var bytes = new Uint8Array( buffer );
            var len = bytes.byteLength;
            for (var i = 0; i < len; i++) {
                binary += String.fromCharCode( bytes[ i ] );
            }
            return  binary;
        },
        onClickTest(){
            const canvas = this.$refs.canvas

            const ctx = canvas.getContext('2d')

            const ret = canvas.toDataURL()



            return
        },
        onClickCut() {
            
            const canvas = this.$refs.canvas
            const ctx = canvas.getContext('2d')

            
            const transform = this.panzoom?.getTransform() ?? {x:0, y:0, scale:0}
            const rect = this.cropRect

            const dstData = ctx.getImageData((rect.x - transform.x) / transform.scale, (rect.y - transform.y) / transform.scale, rect.width / transform.scale, rect.height / transform.scale)                       
            

            let outCanvas = document.createElement('canvas')

            const outCtx = outCanvas.getContext('2d')

            outCanvas.width = dstData.width
            outCanvas.height = dstData.height

            outCtx.putImageData(dstData, 0, 0)

            this.dstImage = outCanvas.toDataURL()

            return
        },
        onMouseDown(event) {                    
            // console.log('m dw')                
            // this.isPressed = true
            // const mousePt = this.downPt = new Pt(event.offsetX, event.offsetY)

            // const crop_canvas = this.$refs.crop_canvas

            // const rw = crop_canvas.clientWidth / crop_canvas.width
            // const rh = crop_canvas.clientHeight / crop_canvas.height

            // const rect = Object.assign(new Rect(), this.cropRect)

            // rect.x *= rw
            // rect.y *= rh
            // rect.width *= rw
            // rect.height *= rw

            // function hitTest(pt) {
            //     return pt.x - gripSize <= mousePt.x &&
            //         pt.y - gripSize <= mousePt.y &&
            //         pt.x + gripSize >= mousePt.x &&
            //         pt.y + gripSize >= mousePt.y
            // }


            // const lt = new Pt(rect.x, rect.y)
            // const t = new Pt(rect.x + (rect.width / 2), rect.y)
            // const rt = new Pt(rect.x + rect.width, rect.y)
            // const l = new Pt(rect.x, rect.y + (rect.height / 2))
            // const r = new Pt(rect.x + rect.width, rect.y + (rect.height / 2))
            // const lb = new Pt(rect.x, rect.y + rect.height)
            // const b = new Pt(rect.x + (rect.width / 2), rect.y + rect.height)
            // const rb = new Pt(rect.x + rect.width, rect.y + rect.height)

            // function create(pt, type) {
            //     return {
            //         pt : pt,
            //         type : type
            //     }
            // }

            // let ptList = [
            //     create(lt, ActionType.lt),
            //     create(t, ActionType.t),
            //     create(rt, ActionType.rt),
            //     create(l, ActionType.l),
            //     create(r, ActionType.r),
            //     create(lb, ActionType.lb),
            //     create(b, ActionType.b),
            //     create(rb, ActionType.rb),
            // ]

            // if(rect.x <= mousePt.x &&
            //     mousePt.x <= rect.x + rect.width &&
            //     rect.y <= mousePt.y &&
            //     mousePt.y <= rect.y + rect.height
            //     )
            //     this.hit = create(mousePt, ActionType.center)
            

            // const hit = ptList.find(el => hitTest(el.pt))

            // if(hit)
            //     this.hit = hit

            // if(this.hit) {
            //     this.srcCropRect = Object.assign({}, this.cropRect)
            //     // canvas.setPointerCapture(1)
            // }

            return
        },
        onMouseMove(event) {

            // const hit = this.hit

            // if(hit) {
            //     const pt = new Pt(event.offsetX, event.offsetY)
                
            //     const crop_canvas = this.$refs.crop_canvas

            //     const rw = crop_canvas.clientWidth / crop_canvas.width
            //     const rh = crop_canvas.clientHeight / crop_canvas.height

            //     const downPt = this.hit.pt
            //     const offset = new Pt(pt.x - downPt.x, pt.y - downPt.y)

            //     offset.x /= rw
            //     offset.y /= rh
                
            //     if(hit.type == ActionType.center) {
            //         this.cropRect.x = this.srcCropRect.x + offset.x
            //         this.cropRect.y = this.srcCropRect.y + offset.y
            //     } else {
            //         if(hit.type & ActionType.l) {
            //             this.cropRect.x = this.srcCropRect.x + offset.x
            //             this.cropRect.width = this.srcCropRect.width - offset.x
            //         } else if(hit.type & ActionType.r) {
            //             this.cropRect.width = this.srcCropRect.width + offset.x
            //         }

            //         if(hit.type & ActionType.t) {
            //             this.cropRect.y = this.srcCropRect.y + offset.y
            //             this.cropRect.height = this.srcCropRect.height - offset.y
            //         } else if(hit.type & ActionType.b) {
            //             this.cropRect.height = this.srcCropRect.height + offset.y
            //         }
            //     }

            //     this.draw()
            // }
            return
        },
        onMouseUp(event) {
            // this.isPressed = false
            // this.hit = null
            return
        },
        onMouseWheel(event) {
            return
        },
        onMouseLeave(event) {
            return
        },
        onMouseOver(event) {
            this.isPressed = false
            this.hit = null

            return
        },
        onMouseOut(event) {
            return
        },
        draw() {
            const canvas = this.$refs.canvas
            const ctx = canvas.getContext('2d')

            const rect = this.cropRect
            const transform = this.panzoom?.getTransform() ?? {x:0, y:0, scale:0}

            const crop_canvas = this.$refs.crop_canvas
            const rw = crop_canvas.clientWidth / crop_canvas.width
            const rh = crop_canvas.clientHeight / crop_canvas.height

            // transform.x *= rw
            // transform.y *= rh

            {
                ctx.clearRect(0,0, canvas.width, canvas.height)
                ctx.drawImage(this.img, 0, 0)

                ctx.strokeStyle = "#FF0000"
                ctx.strokeRect((rect.x - (transform.x / rw)) / transform.scale, (rect.y - (transform.y / rh)) / transform.scale, rect.width / transform.scale, rect.height / transform.scale)
            }

            {
                const canvas = this.$refs.crop_canvas
                const ctx = canvas.getContext('2d')

                ctx.clearRect(0,0, canvas.width, canvas.height)


                ctx.fillStyle = "#0000FF"

                const gripSizeHalf = gripSize / 2
                const gripSizeQuarter = gripSize / 4

                ctx.fillRect(rect.x, rect.y, gripSizeQuarter, gripSizeHalf)
                ctx.fillRect(rect.x, rect.y, gripSizeHalf, gripSizeQuarter)

                ctx.fillRect(rect.x + (rect.width / 2) - gripSizeHalf, rect.y, gripSize, gripSizeQuarter)            

                ctx.fillRect(rect.x + rect.width - gripSizeHalf, rect.y, gripSizeHalf, gripSizeQuarter)
                ctx.fillRect(rect.x + rect.width - gripSizeQuarter, rect.y, gripSizeQuarter, gripSizeHalf)

                ctx.fillRect(rect.x, rect.y + (rect.height / 2) - gripSizeHalf, gripSizeQuarter, gripSize)
                ctx.fillRect(rect.x + rect.width - gripSizeQuarter, rect.y + (rect.height / 2) - gripSizeHalf, gripSizeQuarter, gripSize)

                ctx.fillRect(rect.x, rect.y + rect.height - gripSizeHalf, gripSizeQuarter, gripSizeHalf)
                ctx.fillRect(rect.x, rect.y + rect.height - gripSizeQuarter, gripSizeHalf, gripSizeQuarter)

                ctx.fillRect(rect.x + (rect.width / 2) - gripSizeHalf, rect.y + rect.height - gripSizeQuarter, gripSize, gripSizeQuarter)

                ctx.fillRect(rect.x + rect.width - gripSizeHalf, rect.y + rect.height - gripSizeQuarter, gripSizeHalf, gripSizeQuarter)
                ctx.fillRect(rect.x + rect.width - gripSizeQuarter, rect.y + rect.height - gripSizeHalf, gripSizeQuarter, gripSizeHalf)
            }            
        },
        onPointerDown(event) {
            this.pointerIds.add(event.pointerId)
            console.log('pointer dw ' + event.pointerId)

            const crop_canvas = this.$refs.crop_canvas
            

            if(!this.capturePointId) {
                crop_canvas.setPointerCapture(event.pointerId)
                this.capturePointId = event.pointerId

                this.isPressed = true
                const mousePt = this.downPt = new Pt(event.offsetX, event.offsetY)

                const rw = crop_canvas.clientWidth / crop_canvas.width
                const rh = crop_canvas.clientHeight / crop_canvas.height

                const rect = Object.assign(new Rect(), this.cropRect)

                rect.x *= rw
                rect.y *= rh
                rect.width *= rw
                rect.height *= rw



                const lt = new Pt(rect.x, rect.y)
                const t = new Pt(rect.x + (rect.width / 2), rect.y)
                const rt = new Pt(rect.x + rect.width, rect.y)
                const l = new Pt(rect.x, rect.y + (rect.height / 2))
                const r = new Pt(rect.x + rect.width, rect.y + (rect.height / 2))
                const lb = new Pt(rect.x, rect.y + rect.height)
                const b = new Pt(rect.x + (rect.width / 2), rect.y + rect.height)
                const rb = new Pt(rect.x + rect.width, rect.y + rect.height)


                let ptList = [
                    create(lt, ActionType.lt),
                    create(t, ActionType.t),
                    create(rt, ActionType.rt),
                    create(l, ActionType.l),
                    create(r, ActionType.r),
                    create(lb, ActionType.lb),
                    create(b, ActionType.b),
                    create(rb, ActionType.rb),
                ]

                if(rect.x <= mousePt.x &&
                    mousePt.x <= rect.x + rect.width &&
                    rect.y <= mousePt.y &&
                    mousePt.y <= rect.y + rect.height
                    )
                    this.hit = create(mousePt, ActionType.center)
                

                const hit = ptList.find(el => hitTest(el.pt, mousePt))

                if(hit)
                    this.hit = hit

                if(this.hit) {
                    this.srcCropRect = Object.assign({}, this.cropRect)
                    // canvas.setPointerCapture(1)

                    this.panzoom.pause()
                }
            } else {
                crop_canvas.hasPointerCapture(this.capturePointId)
                this.isPressed = false
                this.hit = null

                this.panzoom.resume()
            }
        },
        onPointerUp(event) {
            this.pointerIds.delete(event.pointerId)
            console.log('pointer up ' + event.pointerId)
            
            if(event.pointerId == this.capturePointId) {                
                const crop_canvas = this.$refs.crop_canvas

                crop_canvas.releasePointerCapture(event.pointerId)
                
                this.isPressed = false
                this.hit = null
                this.capturePointId = null
                
                this.panzoom.resume()
            }        
        },
        onPointerMove(event) {
            console.log('pointer move ' + event.pointerId)

            const hit = this.hit

            if(hit) {
                const pt = new Pt(event.offsetX, event.offsetY)
                
                const crop_canvas = this.$refs.crop_canvas

                const rw = crop_canvas.clientWidth / crop_canvas.width
                const rh = crop_canvas.clientHeight / crop_canvas.height

                const downPt = this.hit.pt
                const offset = new Pt(pt.x - downPt.x, pt.y - downPt.y)

                offset.x /= rw
                offset.y /= rh
                
                if(hit.type == ActionType.center) {
                    this.cropRect.x = this.srcCropRect.x + offset.x
                    this.cropRect.y = this.srcCropRect.y + offset.y
                } else {
                    if(hit.type & ActionType.l) {
                        this.cropRect.x = this.srcCropRect.x + offset.x
                        this.cropRect.width = this.srcCropRect.width - offset.x
                    } else if(hit.type & ActionType.r) {
                        this.cropRect.width = this.srcCropRect.width + offset.x
                    }

                    if(hit.type & ActionType.t) {
                        this.cropRect.y = this.srcCropRect.y + offset.y
                        this.cropRect.height = this.srcCropRect.height - offset.y
                    } else if(hit.type & ActionType.b) {
                        this.cropRect.height = this.srcCropRect.height + offset.y
                    }
                }

                this.draw()
            }
        },
        onPointerCancel(event) {
            this.pointerIds.delete(event.pointerId)
            console.log('pt cancel')
        },        
    }
}
</script>

<style scoped>

.crop_rect {
    position: absolute;
    width: 100px;
    height: 100px;
    border: 1px solid #0000FF;
    user-select: none;
    pointer-events: none;
}

</style>
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
                <ion-button @click="onClickTest" >Test</ion-button>
                <ion-button @click="onClickResize" >Resize</ion-button>
            </div>
            <div style="height: 100px" >
                <img style="height: 100%" :src="dstImage" />
            </div>
            <div ref="container" style="width: auto; height:100vw; position: relative; overflow: hidden">
                <!-- <PinchZoomVue> -->
                <canvas ref="canvas" style="position: absolute; width: 100%; height:100%" >
                    <!-- @mousedown="onMouseDown" @mousemove="onMouseMove" @mouseup="onMouseUp" @mousewheel="onMouseWheel" 
                    @mouseleave="onMouseLeave" @mouseover="onMouseOver" @mouseout="onMouseOut">                     -->
                </canvas>
                <!-- </PinchZoomVue> -->
                <canvas ref="crop_canvas" style="position: absolute; width: 100%; height:100%; touch-action: none"
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
import panzoom, { PanZoomController } from 'panzoom'

import { data } from '@/test/data.js'

const gripSize = 40

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

    const size = gripSize / 2

    return pt.x - size <= mousePt.x &&
        pt.y - size <= mousePt.y &&
        pt.x + size >= mousePt.x &&
        pt.y + size >= mousePt.y
}


function moveValue(value) {
    if(Math.abs(value) < 1)
        return value

    let ret = value

    if(value > 0)
        ret = Math.max(value / 10, 1)
    else if(value < 0)
        ret = Math.min(value / 10, -1)

    return ret
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

            imageItem : null,
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
            pointerIds : new Set(),
            isAnimate : false,
            toAnimateRect : null
        }
    },
    created() {
        console.log('created')
    },
    mounted() {
        console.log('mounted')

        const canvas = this.$refs.canvas
        let img = new Image()
        img.onload = function(event) {
            
            this.size = {
                width : event.target.naturalWidth,
                height : event.target.naturalHeight
            }

            this.cropRect.x = 0;
            this.cropRect.y = 0;

            const crop_canvas = this.$refs.crop_canvas

            crop_canvas.width = this.size.width
            crop_canvas.height = this.size.height

            this.cropRect.width = canvas.width = this.size.width
            this.cropRect.height = canvas.height = this.size.height            


            let ctx = canvas.getContext('2d')
            ctx.drawImage(this.img, 0, 0)

            this.draw()

            this.panzoom = panzoom(canvas, {
                bounds : true,                
                beforeMouseDown : (e) => {
                    return !this.hit == false
                },
                // autocenter : true,
                controller : {
                    getOwner : function() {
                       // return canvas
                        return this.$refs.container
                    }.bind(this),
                    applyTransform : function(transform) {
                        console.log('apply ' + JSON.stringify(transform))


                        const x = transform.x
                        const y = transform.y

                        const canvas = this.$refs.canvas

                        
                        canvas.style.transformOrigin = '0 0 0';
                        canvas.style.transform = 'matrix(' +
                        transform.scale + ', 0, 0, ' +
                        transform.scale + ', ' +
                        transform.x + ', ' + transform.y + ')';
                    }.bind(this),
                    getBBox : function () {
                        return {
                            left: 0,
                            top: 0,
                            width: this.$refs.canvas.clientWidth,
                            height: this.$refs.canvas.clientHeight,
                        };
                    }.bind(this)
                }
            })
        }.bind(this)
        this.img = img

        const json = this.$route.query.json

        if(json) {
            this.imageItem = JSON.parse(json)
            this.init()
            return
        } 

        const item = this.$route.query.item

        if(item) {
            const data = JSON.parse(item)            

            PhotoLibrary.getPhoto(data.id).then(async ret => {
                const buf = await ret.arrayBuffer()

                if(buf && buf.byteLength) {
                    this.img.src = 'data:image;base64,' + btoa(this.arrayBufferToBase64(buf))                    
                    //const temp = btoa(String.fromCharCode.apply(null, new Uint8Array(buf)))

                    return
                }
            })
            return
        }

        const base64 = this.$route.query.base64

        if(base64) {
            this.img.src = 'data:image;base64,' + base64
            return
        }

        
        this.img.src = 'data:image;base64,' + data
    },
    unmounted() {
        return
    },
    methods: {
        async init() {
            const ret = await PhotoGallery.getFile({
                mediumId : this.imageItem.id,
                mediumType : this.imageItem.mediumType,
                mimeType : this.imageItem.mimeType
            })

            this.data = ret.data;

            this.img.src = 'data:' + (this.imageItem.mimeType ?? 'image') + ';base64,'+ ret.byte;
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
            // const canvas = this.$refs.canvas

            // const ctx = canvas.getContext('2d')

            // const ret = canvas.toDataURL()

            // this.panzoom.smoothMoveTo(10, 0)

            this.cropEndAction()

            return
        },
        onClickCut() {
            const canvas = this.$refs.canvas
            const crop_canvas = this.$refs.crop_canvas
            const ctx = canvas.getContext('2d')

            
            const transform = this.panzoom?.getTransform() ?? {x:0, y:0, scale:0}
            const rect = this.cropRect
            
            const rw = crop_canvas.clientWidth / crop_canvas.width
            const rh = crop_canvas.clientHeight / crop_canvas.height

            const dstRect = new Rect((rect.x - (transform.x / rw)) / transform.scale, (rect.y - (transform.y / rh)) / transform.scale, rect.width / transform.scale, rect.height / transform.scale);


            const dstData = ctx.getImageData(dstRect.x, dstRect.y, dstRect.width, dstRect.height)
            
            
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
                // ctx.clearRect(0,0, canvas.width, canvas.height)
                // ctx.drawImage(this.img, 0, 0)
            }

            {
                const canvas = this.$refs.crop_canvas
                const ctx = canvas.getContext('2d')

                ctx.clearRect(0,0, canvas.width, canvas.height)
                ctx.globalAlpha = 0.5
                ctx.fillStyle = "rgba(127, 127, 127, 1)"

                ctx.fillRect(0, 0, canvas.width, canvas.height)

                ctx.fillStyle = "rgba(255, 255, 255, 1)"

                ctx.globalCompositeOperation = "destination-out"
                ctx.globalAlpha = 1

                ctx.fillRect(rect.x, rect.y, rect.width, rect.height)

                ctx.globalCompositeOperation = "source-over"
                ctx.fillStyle = "#0000FF"


                const size = gripSize / rw
                const sizeHalf = size / 2
                const lineHeight = size / 8

                

                ctx.fillRect(rect.x, rect.y, sizeHalf, lineHeight)
                ctx.fillRect(rect.x, rect.y, lineHeight, sizeHalf)                   

                ctx.fillRect(rect.x + (rect.width / 2) - sizeHalf, rect.y, size, lineHeight)            

                ctx.fillRect(rect.x + rect.width - sizeHalf, rect.y, sizeHalf, lineHeight)
                ctx.fillRect(rect.x + rect.width - lineHeight, rect.y, lineHeight, sizeHalf)

                ctx.fillRect(rect.x, rect.y + (rect.height / 2) - sizeHalf, lineHeight, size)
                ctx.fillRect(rect.x + rect.width - lineHeight, rect.y + (rect.height / 2) - sizeHalf, lineHeight, size)

                ctx.fillRect(rect.x, rect.y + rect.height - sizeHalf, lineHeight, sizeHalf)
                ctx.fillRect(rect.x, rect.y + rect.height - lineHeight, sizeHalf, lineHeight)

                ctx.fillRect(rect.x + (rect.width / 2) - sizeHalf, rect.y + rect.height - lineHeight, size, lineHeight)

                ctx.fillRect(rect.x + rect.width - sizeHalf, rect.y + rect.height - lineHeight, sizeHalf, lineHeight)
                ctx.fillRect(rect.x + rect.width - lineHeight, rect.y + rect.height - sizeHalf, lineHeight, sizeHalf)
            }            
        },
        onPointerDown(event) {
            this.pointerIds.add(event.pointerId)
            console.log('pointer dw ' + event.pointerId)

            const crop_canvas = this.$refs.crop_canvas
            

            if(!this.capturePointId) {
                if(this.isAnimate)
                    this.toAnimateRect = null

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

                if(hit) {
                    this.hit = hit
                    this.hit.pt = mousePt
                }

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
                this.cropEndAction()
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

            this.cropEndAction()
        },        
        onClickResize() {
            const canvas = this.$refs.canvas
             
            let outCanvas = document.createElement('canvas')
            const w = canvas.width / 2
            const h = canvas.height / 2
            outCanvas.width = w
            outCanvas.height = h

            const outCtx = outCanvas.getContext('2d')

            outCtx.drawImage(canvas,0, 0, w, h)

            this.dstImage = outCanvas.toDataURL()
            return
        },
        cropEndActionRun() {
            console.log('animate')

            const to = this.toAnimateRect

            if(!to) {
                this.isAnimate = false
                return
            }

            const from = this.cropRect
            

            if(from.x != to.x || from.y != to.y || from.width != to.width || from.height != to.height) {

                from.x += moveValue(to.x - from.x)
                from.y += moveValue(to.y - from.y)

                from.width += moveValue(to.width - from.width)
                from.height += moveValue(to.height - from.height)

                this.cropRect = from

                this.isAnimate = true
                window.requestAnimationFrame(this.cropEndActionRun)
                
            } else {
                this.isAnimate = false
                this.toAnimateRect = null
            }
            
            this.draw()

            return
        },
        cropEndAction() {

            if(!this.isAnimate) {
                const rect = Object.assign(new Rect(), this.cropRect)
                const crop_canvas = this.$refs.crop_canvas

                if(rect.width > crop_canvas.width) {
                    rect.width = crop_canvas.width
                }

                if(rect.height > crop_canvas.height) {
                    rect.height = crop_canvas.height
                }

                if(rect.x < 0) {
                    rect.x = 0
                }
                else if(crop_canvas.width < rect.x + rect.width) {
                    rect.x -= (rect.x + rect.width) - crop_canvas.width
                }
                    
                if(rect.y < 0) {
                    rect.y = 0
                }
                else if(crop_canvas.height < rect.y + rect.height) {
                    rect.y -= (rect.y + rect.height) - crop_canvas.height
                }

                if(this.cropRect.x != rect.x ||
                    this.cropRect.y != rect.y ||
                    this.cropRect.width != rect.width ||
                    this.cropRect.height != rect.height) {
                    this.toAnimateRect = rect

                    this.cropEndActionRun()                
                }
            }

            return
        }
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
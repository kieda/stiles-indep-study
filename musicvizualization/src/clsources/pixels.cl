/**
 * too slow!
 */
float point_segment_distance(float2 p1, float2 p2, float2 point){
    p2 = p2-p1;
    point = point - p1;
    
    float dot_prod = dot(point, p2);
    float projlenSq;
    if(dot_prod<=0.0)
        projlenSq = 0.0;
    else{
        point = p2-point;
        if(dot_prod<=0.0)
            projlenSq = 0.0;
        else
            projlenSq = pown(dot_prod,2)/dot(p2, p2);
    }
    float lenSq = dot(p2, p2) - projlenSq;
    if(lenSq < 0) lenSq = 0;
    return sqrt(lenSq);

}
float this_distance(float x1, float y1,  float x2, float y2,              // the edge
    float x, float y, float s1, float s2){                                // the pixel
    // FUNCTION  - distance(p1, p2)
    //     (x1, y1) = float2 = p1
    //     (x2, y2) = float2 = p2
    // FUNCTION  - exp(n) - returns e^n
    // FUNCTION  - pown(x,y) - returns x^y, where y is an integer (for squared)
    
    float2 p1 = (float2)(x1, y1);
    float2 p2 = (float2)(x2, y2);

    float2 pp = (float2)(x , y);

    float dist = (point_segment_distance(p1, p2, pp) + distance(pp,p1)*s1 + s2*distance(pp,p2));
    
    float dis1 = 5*900/(pown(dist,2)+.1);
    
    return dist;
}
__kernel void sampleKernel(
//    __global const int *x,         __global const int *y,
     __global const int *width,
    __global const float *px,      __global const float *py,
    __global const float *pSiz,

//    __global const int *n_edges,                                   //the number of edges for the size of *px and *py. TODO - figure out how to pass this in.
//    __global const int *n_verts,                                   //the number of vertives for *pSiz. TODO - figure out how to pass this in.


    __global int *ret                                                //the pixels being returned
         ){ //end of params.

    int gid = get_global_id(0);//the current ID. this is used to get the current pixel.
    int x = remainder((float)gid, width[0]);
    int y = gid/width[0];
    float dist_n = this_distance(
           px[0],py[0],px[1],py[1], (float)x, (float)y, (float)pSiz[0], (float)pSiz[1]
        );
    uchar r = (uchar)dist_n;
    uchar g = (uchar)dist_n;
    uchar b = (uchar)dist_n;
    uchar a = 0xFF;
    
    //ret[gid] has full alpha, so 0xFFXXXXXX on all of them.
    ret[gid] = (((int)a)<<24)|(((int)r)<<16)|(b)|(((int)g)<<8);
    //0xFFFF0000 + x[gid] + y[gid];
} 

/**
 * double dist = (l.ptSegDist(x,y) + p1.distance(x,y)*this.p2.size*p2.distance(x,y))/1000d
 * 
 * double dis1 = 5*k/(Math.pow(dist, 2)+c);
 * 
 * r = abs(3*dis1);
 * g = abs(dis1);
 * b = abs(dis1);
 * a = 0xFF;
 *
 * ret[gid] = ( a<<24 )|( r<<16 )|( b )| (g<<8)
 */
 


__kernel void sampleKernel(
    __global const float *info,//the info
    __global float *ret){ 

    int gid = get_global_id(0);
    
    //info[0] = p1size
    //info[1] = p2size
    //info[2] = p1x
    //info[3] = p1y
    //info[4] = p2x
    //info[5] = p2y
    //info[6] = px
    //info[7] = py
    float2 p1 = (float2)(info[2], info[3]);
    float2 p2 = (float2)(info[4], info[5]);
    float2 pp = (float2)(info[6], info[7]);
    p2 -= p1;
    pp -= p1;
    float a = 0;//a affects p1, b affects p2
    float dot_prod = dot(pp, p2);
    float projlenSq;
    if(dot_prod<=0.0){
        projlenSq = 0.0;
        //closest to p1
        a = 1;
    }else{
        pp = p2 - pp;
        dot_prod = dot(pp,p2);
        if(dot_prod<=0.0){
            projlenSq = 0.0;
            a = 0;//closest to p2
        }else{
            float dot_prode = dot(p2, p2);
            projlenSq = half_divide(half_powr(dot_prod,2), dot_prode);
            a = half_sqrt(half_divide(projlenSq, dot_prode));
            //root(projection length squared /  length of segment squared )
        }
    }
    float lenSq = dot(pp, pp) - projlenSq;
    int b = 1 - a;
    if(lenSq < 0) lenSq = 0;
    //float dist = half_sqrt(lenSq);
    

    //((1-a)*p1.size+(1-b)(p2.size))/(Math.pow(l.ptSegDistSq(null))^2);
    ret[gid] = half_divide((a*info[0] + b*info[1]),lenSq);
//    ret[gid] = (((int)a)<<24)|(((int)r)<<16)|(b)|(((int)g)<<8);
} 
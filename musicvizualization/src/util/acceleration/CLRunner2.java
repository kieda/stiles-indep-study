///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//package util.acceleration;
//
///**
// *
// * @author kieda
// */
///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//
///**
// *
// * @author kieda
// */
//
//
//import java.io.File;
//import static org.jocl.CL.*;
//
//import org.jocl.*;
//import util.ReadFile;
//import util.constants.GConstants;
//
///**
// * A small JOCL sample.
// */
//public class CLRunner2 {
//    //<editor-fold defaultstate="collapsed" desc="opening environment">
//    /**
//     * The source code of the OpenCL program to execute
//     */
//    private static String programSource = ReadFile.read(new File("./src/clsources/distance.cl"));
//    /**
//     * The entry point of this sample
//     *
//     */
//    private static Object[] initCL(){
//        // The platform, device type and device number
//        // that will be used
//        final int platformIndex = 0;
//        final long deviceType = CL_DEVICE_TYPE_ALL;
//        final int deviceIndex = 0;
//        
//        // Enable exceptions and subsequently omit error checks in this sample
//        CL.setExceptionsEnabled(true);
//        
//        // Obtain the number of platforms
//        int numPlatformsArray[] = new int[1];
//        clGetPlatformIDs(0, null, numPlatformsArray);
//        int numPlatforms = numPlatformsArray[0];
//        
//        // Obtain a platform ID
//        cl_platform_id platforms[] = new cl_platform_id[numPlatforms];
//        clGetPlatformIDs(platforms.length, platforms, null);
//        cl_platform_id platform = platforms[platformIndex];
//        
//        // Initialize the context properties
//        cl_context_properties contextProperties = new cl_context_properties();
//        contextProperties.addProperty(CL_CONTEXT_PLATFORM, platform);
//        
//        // Obtain the number of devices for the platform
//        int numDevicesArray[] = new int[1];
//        clGetDeviceIDs(platform, deviceType, 0, null, numDevicesArray);
//        int numDevices = numDevicesArray[0];
//        
//        // Obtain a device ID
//        cl_device_id devices[] = new cl_device_id[numDevices];
//        clGetDeviceIDs(platform, deviceType, numDevices, devices, null);
//        cl_device_id device = devices[deviceIndex];
//        
//        // Create a context for the selected device
//        cl_context context = clCreateContext(
//                contextProperties, 1, new cl_device_id[]{device},
//                null, null, null);
//        
//        // Create a command-queue for the selected device
//        cl_command_queue commandQueue =
//                clCreateCommandQueue(context, device, 0, null);
//        return new Object[]{commandQueue, context};
//    }
//    public static void releaseCL(cl_command_queue commandQueue, cl_context context){
//        clReleaseCommandQueue(commandQueue);
//        clReleaseContext(context);
//    }
//    public static void openProgram(){
//        Object[] o = initCL();  commandQueue = (cl_command_queue)o[0];  context = (cl_context)o[1];
//        // Create the program from the source code
//        program = clCreateProgramWithSource(context,
//            1, new String[]{ programSource}, null, null);
//        // Build the program
//        clBuildProgram(program, 0, null, null, null, null);
//        // Create the kernel
//        kernel = clCreateKernel(program, "sampleKernel", null);
//    }
//    //</editor-fold>
//    //<editor-fold defaultstate="collapsed" desc="exit program">
//    public static void exitProgram(){
//        clReleaseKernel(kernel);
//        clReleaseProgram(program);
//        releaseCL(commandQueue, context);
//    }
//    //</editor-fold>
//    //<editor-fold defaultstate="collapsed" desc="static vars">
////    static int n_pixels;
//    static cl_command_queue commandQueue;
//    static cl_context context;
//    static cl_program program;
//    static cl_kernel kernel;
////    static int[] widthi = new int[]{GConstants.SCREEN_WIDTH}; //static int[] yy;
////    static Pointer WIDTH;
////    static cl_mem width;
//    //</editor-fold>
//    
//    //x positions, y positions for pixels
//    public static float executeFX(
//           //list of x positions and y positions for edges
//                float p1size,
//                float p2size,
//                float p1x, float p1y,
//                float p2x, float p2y,
//                float px, float py
//            ){
//        //size_a (p1.size), size_b (p2.size), point1x, point1y; point2x, point2y; pointx, pointy;
//        // Create input- and output data 
//        float dstArray[] = new float[1];//the return value
//        float[] info = new float[]{
//            p1size, p2size, p1x, p1y, p2x, p2y, px, py
//        };
//        Pointer in   = Pointer.to(info);//the information being passed in.  
//        Pointer dst = Pointer.to(dstArray);//3
//
//        
//        //create the CL environment
//        
//        
//        // Allocate the memory objects for the input- and output data
//        cl_mem memObjects[] = new cl_mem[2];
////        memObjects[0] = clCreateBuffer(context, //X and Y
////            CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR,
////            Sizeof.cl_int * n_pixels, X, null);
////        memObjects[1] = clCreateBuffer(context, 
////            CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR,
////            Sizeof.cl_int * n_pixels, Y, null);
//        
//        memObjects[0] = clCreateBuffer(context, //points X and Y
//            CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR,
//            Sizeof.cl_float*info.length, in, null);
//        
//        
//        memObjects[1] = clCreateBuffer(context, //the output
//            CL_MEM_READ_WRITE, 
//            Sizeof.cl_float*dstArray.length, null, null);
//        
//        
//        
//        // Set the arguments for the kernel
//        clSetKernelArg(kernel, 0, 
//            Sizeof.cl_mem, Pointer.to(memObjects[0]));
//        
//        clSetKernelArg(kernel, 1, 
//            Sizeof.cl_mem, Pointer.to(memObjects[1]));
//        
//        // Set the work-item dimensions
//        long global_work_size[] = new long[]{1};
//            //we only execute the function once
//        long local_work_size[] = new long[]{1};
//        
//
//        
//        // Execute the kernel
//        clEnqueueNDRangeKernel(commandQueue, kernel, 1, null,
//            global_work_size, local_work_size, 0, null, null);
//        // Read the output data
//        clEnqueueReadBuffer(commandQueue, memObjects[1], CL_TRUE, 0,
//            dstArray.length * Sizeof.cl_int, dst, 0, null, null);
//        
//        clReleaseMemObject(memObjects[0]);
//        clReleaseMemObject(memObjects[1]);
//        return dstArray[0];
//    }
//}
//
//
//
////    public static void openProgram(int[] x, int[] y){
////        n_pixels = x.length;
////        
////        Object[] o = initCL();  commandQueue = (cl_command_queue)o[0];  context = (cl_context)o[1];
//////        yy = y;
////        WIDTH = Pointer.to(widthi);   //0
//////        Y = Pointer.to(yy);   //0
////        
////        width = clCreateBuffer(context, //X and Y
////            CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR,
////            Sizeof.cl_int, WIDTH, null);
//////        xys[1] = clCreateBuffer(context, 
//////            CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR,
//////            Sizeof.cl_int * n_pixels, Y, null);
////        
////        // Create the program from the source code
////        program = clCreateProgramWithSource(context,
////            1, new String[]{ programSource}, null, null);
////        
////        // Build the program
////        clBuildProgram(program, 0, null, null, null, null);
////        
////        // Create the kernel
////        kernel = clCreateKernel(program, "sampleKernel", null);
////    }
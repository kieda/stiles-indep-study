//package util.acceleration;
//import java.io.File;
//import static org.jocl.CL.*;
//import org.jocl.*;
//import util.ReadFile;
//import util.constants.GConstants;
//public class CLRunner
//{
//    /**
//     * The source code of the OpenCL program to execute
//     */
//    private static String programSource = ReadFile.read(new File("./src/clsources/pixels.cl"));
//    
//
//    /**
//     * The entry point of this sample
//     * 
//     * @param args Not used
//     */
//     static Object[] initCL(){
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
//            contextProperties, 1, new cl_device_id[]{device}, 
//            null, null, null);
//        
//        // Create a command-queue for the selected device
//        cl_command_queue commandQueue = 
//            clCreateCommandQueue(context, device, 0, null);
//        return new Object[]{commandQueue, context};
//    }
//    public static void releaseCL(cl_command_queue commandQueue, cl_context context){
//        clReleaseCommandQueue(commandQueue);
//        clReleaseContext(context);
//    }
//    public static float[] execute(float[] srcArrayA, float[] srcArrayB){
//        // Create input- and output data 
//        int n = srcArrayA.length;
////        float srcArrayA[] = new float[n];
////        float srcArrayB[] = new float[n];
//        float dstArray[] = new float[n];
////        for (int i=0; i<n; i++)
////        {
////            srcArrayA[i] = i;
////            srcArrayB[i] = i;
////        }
//        Pointer srcA = Pointer.to(srcArrayA);
//        Pointer srcB = Pointer.to(srcArrayB);
//        Pointer dst = Pointer.to(dstArray);
//
//        
//        Object[] o = initCL();
//        cl_command_queue commandQueue = (cl_command_queue)o[0];
//        cl_context context = (cl_context)o[1];
//        
//        // Allocate the memory objects for the input- and output data
//        cl_mem memObjects[] = new cl_mem[3];
//        memObjects[0] = clCreateBuffer(context, 
//            CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR,
//            Sizeof.cl_float * n, srcA, null);
//        memObjects[1] = clCreateBuffer(context, 
//            CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR,
//            Sizeof.cl_float * n, srcB, null);
//        memObjects[2] = clCreateBuffer(context, 
//            CL_MEM_READ_WRITE, 
//            Sizeof.cl_float * n, null, null);
//        
//        // Create the program from the source code
//        cl_program program = clCreateProgramWithSource(context,
//            1, new String[]{ programSource }, null, null);
//        
//        // Build the program
//        clBuildProgram(program, 0, null, null, null, null);
//        
//        // Create the kernel
//        cl_kernel kernel = clCreateKernel(program, "sampleKernel", null);
//        
//        // Set the arguments for the kernel
//        clSetKernelArg(kernel, 0, 
//            Sizeof.cl_mem, Pointer.to(memObjects[0]));
//        clSetKernelArg(kernel, 1, 
//           Sizeof.cl_mem, Pointer.to(memObjects[1]));
//        clSetKernelArg(kernel, 2, 
//            Sizeof.cl_mem, Pointer.to(memObjects[2]));
//        
//        // Set the work-item dimensions
//        long global_work_size[] = new long[]{n};
//        long local_work_size[] = new long[]{1};
//        
////        long time = System.currentTimeMillis();
//        // Execute the kernel
//        clEnqueueNDRangeKernel(commandQueue, kernel, 1, null,
//            global_work_size, local_work_size, 0, null, null);
//        
//        // Read the output data
//        clEnqueueReadBuffer(commandQueue, memObjects[2], CL_TRUE, 0,
//            n * Sizeof.cl_float, dst, 0, null, null);
////        System.out.println(System.currentTimeMillis()-time);
//        
//        
//        
//        
//        
//        // Release kernel, program, and memory objects
//        clReleaseMemObject(memObjects[0]);
//        clReleaseMemObject(memObjects[1]);
//        clReleaseMemObject(memObjects[2]);
//        clReleaseKernel(kernel);
//        clReleaseProgram(program);
//        releaseCL(commandQueue, context);
//        
//        // Verify the result
////        boolean passed = true;
////        final float epsilon = 1e-7f;
////        for (int i=0; i<n; i++)
////        {
////            float x = dstArray[i];
////            float y = srcArrayA[i] * srcArrayB[i];
////            boolean epsilonEqual = Math.abs(x - y) <= epsilon * Math.abs(x);
////            if (!epsilonEqual)
////            {
////                passed = false;
////                break;
////            }
////        }
////        System.out.println("Test "+(passed?"PASSED":"FAILED"));
//    //    if (n <= 10)
////        {
////            System.out.println("Result: "+java.util.Arrays.toString(dstArray));
////        }
//        return dstArray;
//    }
//    static int n_pixels;
////    static cl_mem xyvals[] = new cl_mem[2];
//    static cl_command_queue commandQueue;
//    static cl_context context;
//    static cl_program program;
//    static cl_kernel kernel;
//    static int[] widthi = new int[]{GConstants.SCREEN_WIDTH}; //static int[] yy;
//    static Pointer WIDTH;
////    static Pointer Y;
//    static cl_mem width;
//    public static void openProgram(int[] x, int[] y){
//        n_pixels = x.length;
//        
//        Object[] o = initCL();  commandQueue = (cl_command_queue)o[0];  context = (cl_context)o[1];
////        yy = y;
//        WIDTH = Pointer.to(widthi);   //0
////        Y = Pointer.to(yy);   //0
//        
//        width = clCreateBuffer(context, //X and Y
//            CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR,
//            Sizeof.cl_int, WIDTH, null);
////        xys[1] = clCreateBuffer(context, 
////            CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR,
////            Sizeof.cl_int * n_pixels, Y, null);
//        
//        // Create the program from the source code
//        program = clCreateProgramWithSource(context,
//            1, new String[]{ programSource}, null, null);
//        
//        // Build the program
//        clBuildProgram(program, 0, null, null, null, null);
//        
//        // Create the kernel
//        kernel = clCreateKernel(program, "sampleKernel", null);
//    }
//    public static void exitProgram(){
//        clReleaseKernel(kernel);
//        clReleaseProgram(program);
//        releaseCL(commandQueue, context);
//    }//x positions, y positions for pixels
//    public static int[] executePix(//int[] x, int[] y, //float[] rand
//           //list of x positions and y positions for edges
//            float[] px, float[] py, float[] pSiz
//            ){
//        
//        // Create input- and output data 
//        int n_edges = px.length;//also == py.length. I trust I have the correct inputs.
//                                //the number of edges
//        int n_verts = pSiz.length;//the number of vertices
//        
//        int dstArray[] = new int[n_pixels];//n number of pixels
////        
//        Pointer PX   = Pointer.to(px);  //2
//        Pointer PY   = Pointer.to(py);  //3
//        Pointer PSIZ = Pointer.to(pSiz);//4
//        
//        Pointer dst = Pointer.to(dstArray);//3
//
//        
//      //create the CL environment
//        
//        
//        // Allocate the memory objects for the input- and output data
//        cl_mem memObjects[] = new cl_mem[4];
////        memObjects[0] = clCreateBuffer(context, //X and Y
////            CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR,
////            Sizeof.cl_int * n_pixels, X, null);
////        memObjects[1] = clCreateBuffer(context, 
////            CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR,
////            Sizeof.cl_int * n_pixels, Y, null);
//        
//        memObjects[0] = clCreateBuffer(context, //points X and Y
//            CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR,
//          Sizeof.cl_float * n_edges, PX, null);
//        memObjects[1] = clCreateBuffer(context, 
//            CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR,
//            Sizeof.cl_float* n_edges, PY, null);
//        
//        memObjects[2] = clCreateBuffer(context, //size of the Verts
//            CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR,
//            Sizeof.cl_float*n_verts, PSIZ, null);
//        
//      
//        memObjects[3] = clCreateBuffer(context, //the output
//          CL_MEM_READ_WRITE, 
//            Sizeof.cl_int*n_pixels, null, null);
//        
//        
//        
//      // Set the arguments for the kernel
//        clSetKernelArg(kernel, 0, 
//            Sizeof.cl_mem, Pointer.to(width));
//        
//        clSetKernelArg(kernel, 1, 
//            Sizeof.cl_mem, Pointer.to(memObjects[0]));
//        clSetKernelArg(kernel, 2, 
//            Sizeof.cl_mem, Pointer.to(memObjects[1]));
//        clSetKernelArg(kernel, 3, 
//            Sizeof.cl_mem, Pointer.to(memObjects[2]));
////        clSetKernelArg(kernel, 5, 
////            Sizeof.cl_int, Pointer.to(new int[]{n_edges}));
////        clSetKernelArg(kernel, 6, 
////            Sizeof.cl_int, Pointer.to(new int[]{n_verts}));
//        clSetKernelArg(kernel, 4, 
//            Sizeof.cl_mem, Pointer.to(memObjects[3]));
//        
//        // Set the work-item dimensions
//        long global_work_size[] = new long[]{n_pixels};
//        long local_work_size[] = new long[]{1};
//        
////        long time = System.currentTimeMillis();
//        // Execute the kernel
//        clEnqueueNDRangeKernel(commandQueue, kernel, 1, null,
//            global_work_size, local_work_size, 0, null, null);
//        
//        // Read the output data
//        clEnqueueReadBuffer(commandQueue, memObjects[3], CL_TRUE, 0,
//            n_pixels * Sizeof.cl_int, dst, 0, null, null);
////        System.out.println(System.currentTimeMillis()-time);
//        
//        // Release kernel, program, and memory objects
////        clReleaseMemObject(memObjects[0]);
////        clReleaseMemObject(memObjects[1]);
//        clReleaseMemObject(memObjects[0]);
//        clReleaseMemObject(memObjects[1]);
//        clReleaseMemObject(memObjects[2]);
//        clReleaseMemObject(memObjects[3]);
//        
//        
//        // Verify the result
////        boolean passed = true;
////        final float epsilon = 1e-7f;
////        for (int i=0; i<n; i++)
////        {
////            float x = dstArray[i];
////            float y = srcArrayA[i] * srcArrayB[i];
////            boolean epsilonEqual = Math.abs(x - y) <= epsilon * Math.abs(x);
////            if (!epsilonEqual)
////            {
////                passed = false;
////                break;
////            }
////        }
////        System.out.println("Test "+(passed?"PASSED":"FAILED"));
//    //    if (n <= 10)
////        {
////            System.out.println("Result: "+java.util.Arrays.toString(dstArray));
////        }
//        return dstArray;
//    }
//}

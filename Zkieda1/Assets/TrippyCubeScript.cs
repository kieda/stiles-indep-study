using UnityEngine;
using System.Collections;

public class TrippyCubeScript : MonoBehaviour
{
    public GameObject movingCube;
    public int width;
    public int height;
    private GameObject[] cubeclones;

    public ComputeShader shader;
    private int kernelHandle;
    private ComputeBuffer trippyBuffer;


    public float alpha;
    public float lambda;

    void Start()
    {
        cubeclones = new GameObject[width * height];
        for (int x = 0; x < width; x++)
        {
            for (int y = 0; y < height; y++)
            {
                GameObject cubeClone = (GameObject)Instantiate(movingCube);
                cubeClone.transform.parent = this.transform;
                cubeClone.transform.Translate(x * 1.1f, 0f, y * 1.1f);
                cubeclones[y * width + x] = cubeClone;
            }
        }

        kernelHandle = shader.FindKernel("HeightMatcher");
        trippyBuffer = new ComputeBuffer(width * height, sizeof(float));
    }

    //todo keep track of position of camera, update as necessary.
    // eg -- round current position. change last row of game object's transforms to fit new y, x. 
    // update our algorithm

    // Update is called once per frame
    public GameObject personPosition;

    void Update()
    {
        alpha += Time.deltaTime/ (255.0f);
        Debug.Log(alpha);

        Vector3 currentPos = personPosition.transform.position;
        float[] pMid;

        {
            
            int x = (Mathf.FloorToInt(currentPos.x / 1.1f));
            int y = (Mathf.FloorToInt(currentPos.z / 1.1f));
            transform.position = new Vector3(x - width / 2f, 0, y - height / 2f);

            //middle point on the screen
            pMid = new float[]{ (float)(width / 2.0f - x), (float)(height / 2.0f - y)};
        }
        

        //this is the screen length squared, from the middle point
        float screenLength = pMid[0] * pMid[0] + pMid[1] * pMid[1];

        //this is the alpha -- adjusted to the current lambda value
        float alphaPrime = alpha * Mathf.Pow(screenLength, 1.0f - lambda);
        shader.SetBuffer(kernelHandle, "Result", trippyBuffer);
        //    shader.SetTexture(kernelHandle, "Result", trippyTexture);
        shader.SetFloats("pMid", pMid);
        shader.SetFloat("alpha", alphaPrime);
        shader.SetFloat("lambda", lambda);

        shader.Dispatch(kernelHandle, width/4, height/4, 1);
        
        float[] data = new float[width * height];
        trippyBuffer.GetData(data);
        

        //personPosition.transform.position = new Vector3(currentPos.x, cubePos + 10.4f, currentPos.z);


        for (int x = 0; x < width; x++) {
            for(int y = 0; y < height; y++){
                float cubePos = (float)(5.0 * data[y * width + x] / 255.0);
                
                GameObject cube = cubeclones[y * width + x];
                Vector3 pos = cube.transform.position;
                cube.transform.position = new Vector3(pos.x, cubePos, pos.z);
               
            }
        }
    }
}

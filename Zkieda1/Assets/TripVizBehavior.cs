using UnityEngine;
using System.Collections;

public class TripVizBehavior : MonoBehaviour {
	//attach our shader here
	public ComputeShader shader;
	private int kernelHandle;
	private RenderTexture trippyTexture;


	public float alpha;
	public float lambda;

	public int width;
	public int height;
    public Renderer rend;

	// Use this for initialization
	void Start () {
		Debug.Log ("starting trippy");


		kernelHandle = shader.FindKernel("HeightMatcher");

        //we only need to create this once.
        
		trippyTexture = new RenderTexture (width, height, 0);
		trippyTexture.enableRandomWrite = true;
		trippyTexture.Create ();

		
        rend = GetComponent<Renderer>();
        
        
	}

    public Texture2D tex;
	// Update is called once per frame
	void Update () {
		//Debug.Log ("updating : " + alpha);
        alpha += Time.deltaTime / (100.0f * 255.0f);
        Debug.Log(Time.smoothDeltaTime);

		//middle point on the screen
		float[] pMid = { (float)width / 2.0f, (float)height / 2.0f };

		//this is the screen length squared, from the middle point
		float screenLength = pMid [0] * pMid [0] + pMid [1] * pMid [1];

		//this is the alpha -- adjusted to the current lambda value
		float alphaPrime = alpha * Mathf.Pow(screenLength, 1.0f - lambda);

        shader.SetTexture(kernelHandle, "Result", trippyTexture);
        shader.SetFloats ("pMid", pMid);
		shader.SetFloat ("alpha", alphaPrime);
		shader.SetFloat ("lambda", lambda);

		shader.Dispatch (kernelHandle, width / 8, height / 8, 1);


        // For testing purposes, also write to a file in the project folder
        // File.WriteAllBytes(Application.dataPath + "/../SavedScreen.png", bytes);
        rend.material.mainTexture = trippyTexture;
       // Debug.Log(rend.material.HasProperty("_PARALLAXMAP"));
 
 //       rend.material.EnableKeyword("_PARALLAXMAP");
    //    rend.material.SetTexture("_PARALLAXMAP", tex);
        Debug.Log(rend.materials.Length);
        //	transform.Find ("SphereTex").GetComponent<MeshRenderer> ().material.mainTexture = trippyTexture;
    }
}

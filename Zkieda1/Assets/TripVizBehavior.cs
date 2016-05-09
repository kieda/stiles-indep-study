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

	// Use this for initialization
	void Start () {
		Debug.Log ("starting trippy");


		kernelHandle = shader.FindKernel("CSMain");

		//we only need to create this once. 
		trippyTexture = new RenderTexture (width, height, 24);
		trippyTexture.format = RenderTextureFormat.ARGB32;
		trippyTexture.enableRandomWrite = true;
		trippyTexture.Create ();

		shader.SetTexture (kernelHandle, "Result", trippyTexture);

	}


	
	// Update is called once per frame
	void Update () {
		Debug.Log ("updating");

		//middle point on the screen
		float[] pMid = { (float)width / 2.0f, (float)height / 2.0f };

		//this is the screen length squared, from the middle point
		float screenLength = pMid [0] * pMid [0] + pMid [1] * pMid [1];

		//this is the alpha -- adjusted to the current lambda value
		float alphaPrime = alpha * Mathf.Pow(screenLength, 1.0f - lambda);

		shader.SetFloats ("pMid", pMid);
		shader.SetFloat ("alpha", alphaPrime);
		shader.SetFloat ("lambda", lambda);

		shader.Dispatch (kernelHandle, width / 32, height / 16, 1);
		transform.Find ("SphereTex").GetComponent<MeshRenderer> ().material.mainTexture = trippyTexture;
	}
}

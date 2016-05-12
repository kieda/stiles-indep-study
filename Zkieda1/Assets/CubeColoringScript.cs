using UnityEngine;
using System.Collections;
using System.Collections.Generic;

public class CubeColoringScript : MonoBehaviour {

	// Use this for initialization
	void Start () {
        Debug.Log("asdf");
        Mesh mesh = GetComponent<MeshFilter>().mesh;
        Vector3[] vertices = mesh.vertices;
        
       // Color[] colors = new Color[vertices.Length];
        Color top = new Color(26f / 245f, 233f /245f, 233f/245f);
        Color bottom = top * .5f;
        List<Color> colors = new List<Color>();
        for(int i = 0; i < vertices.Length; i++)
        {
            colors.Add(Color.Lerp(bottom, top, (vertices[i].y + .5f)));
          //  colors[i] = ;
        }
        mesh.SetColors(colors);
      //  mesh.colors = colors;
    }
}

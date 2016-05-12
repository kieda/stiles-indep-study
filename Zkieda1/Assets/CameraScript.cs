using UnityEngine;
using System.Collections;

using System;

public class CameraScript : MonoBehaviour {
    public float flySpeed = .01f;
    public bool bindPos = true; 

    void Start () {
        Cursor.visible = false;
    }
    float rotationY = 0F;
    public float sensitivityX = 15F;
    public float sensitivityY = 15F;
    public float minimumX = -360F;
    public float maximumX = 360F;

    public float minimumY = -60F;
    public float maximumY = 60F;

    void Update () {
        if (Input.GetKey(KeyCode.W))
        {
            float y = transform.position.y;
            transform.Translate(Vector3.forward * flySpeed);
            transform.position = new Vector3(transform.position.x, y, transform.position.z);
            
        }
        if (Input.GetKey(KeyCode.S))
        {
            float y = transform.position.y;
            transform.Translate(Vector3.back * flySpeed);
            transform.position = new Vector3(transform.position.x, y, transform.position.z);
        }
        if (Input.GetKey(KeyCode.A))
        {
            float y = transform.position.y;
            transform.Translate(Vector3.left * flySpeed);
            transform.position = new Vector3(transform.position.x, y, transform.position.z);
        }
        if (Input.GetKey(KeyCode.D))
        {
            float y = transform.position.y;
            transform.Translate(Vector3.right * flySpeed);
            transform.position = new Vector3(transform.position.x, y, transform.position.z);
        }

        if (bindPos && transform.position.magnitude > 1)
        {
            transform.position = transform.position.normalized;
        }

        float rotationX = transform.localEulerAngles.y + Input.GetAxis("Mouse X") * sensitivityX;

        rotationY += Input.GetAxis("Mouse Y") * sensitivityY;
        rotationY = Mathf.Clamp(rotationY, minimumY, maximumY);

        transform.localEulerAngles = new Vector3(-rotationY, rotationX, 0);
    }
}

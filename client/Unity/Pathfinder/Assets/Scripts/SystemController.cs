using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.XR.ARFoundation;

public class SystemController : MonoBehaviour
{
    [SerializeField]
    private ARSessionOrigin arSessionOrigin;
    // Start is called before the first frame update
    void Start()
    {
        arSessionOrigin.transform.Rotate(0, -90, 0);
    }

    // Update is called once per frame
    void Update()
    {
        if (Input.GetKey(KeyCode.Escape))
        {
            Application.Quit();
        }
    }
}

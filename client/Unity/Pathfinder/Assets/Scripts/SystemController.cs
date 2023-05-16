
using System;
using TMPro;
using UnityEngine;
using UnityEngine.XR.ARFoundation;

class UserCameraInfo
{
    public float x;
    public float y;
    public float z;
    public float azimuth;
    public float pitch;
    public float roll;
}

public class SystemController : MonoBehaviour
{
    [SerializeField]
    private ARSessionOrigin arSessionOrigin;
    [SerializeField]
    private GameObject map;
    [SerializeField]
    private GameObject controlPoint;
    
    // Start is called before the first frame update
    void Start()
    {
        Input.compass.enabled = true;
        Input.gyro.enabled = true;
        arSessionOrigin.MakeContentAppearAt(arSessionOrigin.transform, controlPoint.transform.position);
    }

    // Update is called once per frame
    void Update()
    {   
        if (Input.GetKey(KeyCode.Escape))
        {
            Application.Quit();
        }
    }

    private void InitARCameraTransform(string args)
    {
        UserCameraInfo userCameraInfo = JsonUtility.FromJson<UserCameraInfo>(args);

        if (arSessionOrigin == null)
        {
            Debug.LogError("ARSessionOrigin component not assigned.");
            return;
        }

        // Init Camera Position

        Vector3 userPosition = new Vector3(userCameraInfo.x, userCameraInfo.y + 1.8f, userCameraInfo.z);
        arSessionOrigin.transform.position = controlPoint.transform.position + userPosition;
        arSessionOrigin.transform.rotation = Quaternion.Euler(0, (userCameraInfo.azimuth - arSessionOrigin.camera.transform.localRotation.eulerAngles.y + 360) % 360, 0);
    }

    private void SetARCameraAngle(string args)
    {
        UserCameraInfo userCameraInfo = JsonUtility.FromJson<UserCameraInfo>(args);

        if (arSessionOrigin == null)
        {
            Debug.LogError("ARSessionOrigin component not assigned.");
            return;
        }

        // Init Camera Position

        arSessionOrigin.transform.rotation = Quaternion.Euler(0, (userCameraInfo.azimuth - arSessionOrigin.camera.transform.localRotation.eulerAngles.y + 360) % 360, 0);
    }

    private void ToggleMapVisibility(string args)
    {
        map.SetActive(!map.activeSelf);
    }

    private void SetARCameraPosition(string args)
    {
        UserCameraInfo userCameraInfo = JsonUtility.FromJson<UserCameraInfo>(args);

        if (arSessionOrigin == null)
        {
            Debug.LogError("ARSessionOrigin component not assigned.");
            return;
        }
        Vector3 userPosition = new Vector3(userCameraInfo.x, userCameraInfo.y + 1.8f, userCameraInfo.z);
        arSessionOrigin.transform.position = controlPoint.transform.position + userPosition;
    }


}

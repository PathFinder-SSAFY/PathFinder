
using Newtonsoft.Json.Linq;
using System;
using System.Collections.Generic;
using System.Linq;
using TMPro;
using Unity.VisualScripting;
using UnityEngine;
using UnityEngine.AI;
using UnityEngine.XR.ARFoundation;

class Point
{
    public float x;
    public float y;
    public float z;
}

public class SetNavigationTarget : MonoBehaviour
{
    /*[SerializeField]
    private Camera topDownCamera;*/
    [SerializeField]
    private GameObject navTargetObject;
    [SerializeField]
    private GameObject controlPoint;
    [SerializeField]
    private ARSessionOrigin arSessionOrigin;

    private NavMeshPath path;
    private LineRenderer line;
    private List<Vector3> pathList = new List<Vector3>();
    private int count = 0;

    // Start is called before the first frame update
    void Start()
    {
        path = new NavMeshPath();
        line = transform.GetComponent<LineRenderer>();
    }

    // Update is called once per frame
    void Update()
    {
        Vector3 userPosition = arSessionOrigin.transform.position + arSessionOrigin.camera.transform.localPosition;
        
        userPosition.x = Mathf.Round(userPosition.x);
        userPosition.y = Mathf.Round(userPosition.y - 1.8f);
        userPosition.z = Mathf.Round(userPosition.z);

        double distance = Vector3.Distance(userPosition, pathList.Last());

        if (distance < 1)
        {
            //力老 啊鳖款 芭 昏力
            pathList.RemoveAt(pathList.Count - 1);
            line.positionCount--;
        }
    }

    private void FixedUpdate()
    {
        if (count > 50)
        {
            count = 0;
            SendCameraPositionToAndroid("");
        }
        else count++;
    }

    void SetNavigationPath(string args)
    {
        JArray points = JArray.Parse(args);
        
        pathList.Clear();
        foreach (JObject item in points)
        {
            pathList.Add(new Vector3(
                    float.Parse(item.GetValue("x").ToString()),
                    float.Parse(item.GetValue("y").ToString()) + 0.1f,
                    float.Parse(item.GetValue("z").ToString())
                ) + controlPoint.transform.position);
        }
        navTargetObject.transform.position = pathList.First() + new Vector3(0, 1.8f, 0);

        line.enabled = true;
        line.positionCount = 0;
        for (int i = 0; i < points.Count; i++)
        {
            line.SetPosition(line.positionCount++, pathList[i]);
        }
    }

    private void SendCameraPositionToAndroid(string args)
    {
        Vector3 arCameraPosition = arSessionOrigin.transform.position - controlPoint.transform.position
            + new Vector3(
                arSessionOrigin.camera.transform.localPosition.z,
                0,
                -arSessionOrigin.camera.transform.localPosition.x
                );
            
        String result = "{ " +
            "\"x\": " + arCameraPosition.x + "," +
            "\"y\": " + 0 + "," +
            "\"z\": " + arCameraPosition.z +
            "}";


        using (AndroidJavaClass unityPlayer = new AndroidJavaClass("com.unity3d.player.UnityPlayer"))
        {
            using (AndroidJavaObject currentActivity = unityPlayer.GetStatic<AndroidJavaObject>("currentActivity"))
            {
                currentActivity.Call("getCameraPositionFromUnity", result);
            }
        }
    }

}

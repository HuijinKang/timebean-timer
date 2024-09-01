#include <stdio.h>
#include <windows.h>
#include <math.h>
#include "jni_mouse_MouseHookJni.h"

// 전역 변수
HHOOK g_mouseHook = NULL;
JavaVM *g_jvm = NULL;
JNIEnv *g_env = NULL;
jobject g_javaObject = NULL;
POINT g_lastMousePos = {0};

// 마우스 후킹 프로시저
LRESULT CALLBACK MouseHookProc(int nCode, WPARAM wParam, LPARAM lParam) {
    if (nCode == HC_ACTION) {
        switch (wParam) {
            case WM_MOUSEMOVE: {
                MSLLHOOKSTRUCT *hookStruct = (MSLLHOOKSTRUCT *)lParam;
                POINT currentMousePos = hookStruct->pt;

                // 마우스 이동량 계산 (유클리드 거리 사용)
                int distance = (int)sqrt(pow(currentMousePos.x - g_lastMousePos.x, 2) + pow(currentMousePos.y - g_lastMousePos.y, 2));

                // 일정 거리 이상 마우스 이동 시에만 후킹 실행
                if (distance >= 300) {
                    if (g_env != NULL && g_javaObject != NULL) {
                        jclass cls = (*g_env)->GetObjectClass(g_env, g_javaObject);
                        jmethodID mid = (*g_env)->GetMethodID(g_env, cls, "handleMouseEvent", "(II)V");

                        if (mid != NULL) {
                            // 마우스 이동 이벤트 전달
                            (*g_env)->CallVoidMethod(g_env, g_javaObject, mid, wParam, lParam);
                        }
                    }

                    // 마지막 마우스 위치 업데이트
                    g_lastMousePos = currentMousePos;
                }
                break;
            }
            case WM_LBUTTONDOWN:
            case WM_RBUTTONDOWN:
            case WM_MBUTTONDOWN:
            case WM_LBUTTONUP:
            case WM_RBUTTONUP:
            case WM_MBUTTONUP: {
                if (g_env != NULL && g_javaObject != NULL) {
                    jclass cls = (*g_env)->GetObjectClass(g_env, g_javaObject);
                    jmethodID mid = (*g_env)->GetMethodID(g_env, cls, "handleMouseEvent", "(II)V");

                    if (mid != NULL) {
                        // 마우스 클릭 이벤트 전달
                        (*g_env)->CallVoidMethod(g_env, g_javaObject, mid, wParam, lParam);
                    }
                }
                break;
            }
        }
    }

    return CallNextHookEx(NULL, nCode, wParam, lParam);
}

// 마우스 후킹 시작 함수
JNIEXPORT void JNICALL Java_jni_mouse_MouseHookJni_startMouseHook(JNIEnv *env, jobject obj) {
    // JVM 및 JNIEnv 설정
    (*env)->GetJavaVM(env, &g_jvm);
    g_env = env;
    g_javaObject = (*env)->NewGlobalRef(env, obj);

    // 마우스 후킹 설정
    g_mouseHook = SetWindowsHookEx(WH_MOUSE_LL, MouseHookProc, GetModuleHandle(NULL), 0);

    if (g_mouseHook == NULL) {
        printf("Failed to install mouse hook\n");
    } else {
        printf("Mouse hook installed\n");

        // 메시지 루프 시작
        MSG msg;
        while (GetMessage(&msg, NULL, 0, 0)) {
            TranslateMessage(&msg);
            DispatchMessage(&msg);
        }
    }

    // 후킹 해제
    UnhookWindowsHookEx(g_mouseHook);
    g_mouseHook = NULL;

    if (g_javaObject != NULL) {
        (*env)->DeleteGlobalRef(env, g_javaObject);
        g_javaObject = NULL;
    }

    g_env = NULL;
}

// 마우스 후킹 종료 함수 (필요할 경우 사용)
JNIEXPORT void JNICALL Java_jni_mouse_MouseHookJni_stopMouseHook(JNIEnv *env, jobject obj) {
    if (g_mouseHook != NULL) {
        UnhookWindowsHookEx(g_mouseHook);
        g_mouseHook = NULL;
    }

    if (g_javaObject != NULL) {
        (*env)->DeleteGlobalRef(env, g_javaObject);
        g_javaObject = NULL;
    }

    g_env = NULL;
}

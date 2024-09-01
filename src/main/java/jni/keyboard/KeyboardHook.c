#include <stdio.h>
#include <windows.h>
#include "jni_keyboard_KeyboardHookJni.h"

// 전역 변수
HHOOK g_keyboardHook = NULL;
JavaVM *g_jvm = NULL; // JVM 인스턴스를 저장할 전역 변수
JNIEnv *g_env = NULL;
jobject g_javaObject = NULL;

// 키보드 후킹 프로시저
LRESULT CALLBACK KeyboardHookProc(int nCode, WPARAM wParam, LPARAM lParam) {
    if (nCode == HC_ACTION) {
        BOOL isKeyDown = (lParam & (1 << 31)) == 0; // 키가 눌렸는지 여부 확인

        if (g_env != NULL && g_javaObject != NULL) {
            jclass cls = (*g_env)->GetObjectClass(g_env, g_javaObject);
            jmethodID mid = (*g_env)->GetMethodID(g_env, cls, "handleKeyEvent", "(Z)V");

            if (mid != NULL) {
                (*g_env)->CallVoidMethod(g_env, g_javaObject, mid, isKeyDown);
            }
        }
    }

    return CallNextHookEx(NULL, nCode, wParam, lParam);
}

// 키보드 후킹 시작 함수
JNIEXPORT void JNICALL Java_jni_keyboard_KeyboardHookJni_startKeyboardHook(JNIEnv *env, jobject obj) {
    // JVM 및 JNIEnv 설정
    (*env)->GetJavaVM(env, &g_jvm);
    g_env = env;
    g_javaObject = (*env)->NewGlobalRef(env, obj);

    // 키보드 후킹 설정
    g_keyboardHook = SetWindowsHookEx(WH_KEYBOARD_LL, KeyboardHookProc, GetModuleHandle(NULL), 0);

    if (g_keyboardHook == NULL) {
        printf("Failed to install keyboard hook\n");
    } else {
        printf("Keyboard hook installed\n");

        // 메시지 루프 시작
        MSG msg;
        while (GetMessage(&msg, NULL, 0, 0)) {
            TranslateMessage(&msg);
            DispatchMessage(&msg);
        }
    }

    // 후킹 해제
    UnhookWindowsHookEx(g_keyboardHook);
}

// 키보드 후킹 종료 함수 (필요할 경우 사용)
JNIEXPORT void JNICALL Java_jni_keyboard_KeyboardHookJni_stopKeyboardHook(JNIEnv *env, jobject obj) {
    if (g_keyboardHook != NULL) {
        UnhookWindowsHookEx(g_keyboardHook);
        g_keyboardHook = NULL;
    }

    if (g_javaObject != NULL) {
        (*env)->DeleteGlobalRef(env, g_javaObject);
        g_javaObject = NULL;
    }

    g_env = NULL;
}

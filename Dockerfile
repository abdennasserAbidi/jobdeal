    # Use a base image with Android SDK pre-installed
    FROM openjdk:17-jdk-slim

    # Install necessary Android SDK components (adjust versions as needed)
    ENV ANDROID_SDK_ROOT /opt/android-sdk
    RUN mkdir -p $ANDROID_SDK_ROOT && \
        wget -q https://dl.google.com/android/repository/commandlinetools-linux-8583976_latest.zip -O android-sdk.zip && \
        unzip -q android-sdk.zip -d $ANDROID_SDK_ROOT/cmdline-tools && \
        rm android-sdk.zip

    ENV PATH=$PATH:$ANDROID_SDK_ROOT/cmdline-tools/latest/bin:$ANDROID_SDK_ROOT/platform-tools
    RUN yes | sdkmanager --licenses && \
        sdkmanager "platforms;android-34" "build-tools;34.0.0" "platform-tools" "extras;android;m2repository" "extras;google;m2repository"

    # Set working directory
    WORKDIR /app

    # Copy your project files
    COPY . /app

    # Build the Android project
    RUN ./gradlew assembleRelease
FROM openjdk:17-jdk-slim

# --- Install Godot using glibc compatibility ---

# Arguments for versions (easier to update)
ARG GODOT_VERSION=4.2.2
# Find download URLs from godotengine.org or TuxFamily for v4.x
ARG GODOT_DOWNLOAD_URL=https://github.com/godotengine/godot-builds/releases/download/${GODOT_VERSION}-stable/Godot_v${GODOT_VERSION}-stable_linux.x86_64.zip
ARG GODOT_TEMPLATES_URL=https://github.com/godotengine/godot-builds/releases/download/${GODOT_VERSION}-stable/Godot_v${GODOT_VERSION}-stable_export_templates.tpz
ARG GODOT_EXPECTED_BINARY=Godot_v${GODOT_VERSION}-stable_linux.x86_64

# Combine installs into one layer
RUN apt-get update && \
    apt-get install -y --no-install-recommends curl unzip ca-certificates bash libfontconfig1 && \
    rm -rf /var/lib/apt/lists/* && \
    # Download Godot and templates
    echo "Downloading Godot from ${GODOT_DOWNLOAD_URL}" && \
    curl -L ${GODOT_DOWNLOAD_URL} -o /tmp/godot-binary.zip && \
    echo "Downloading Templates from ${GODOT_TEMPLATES_URL}" && \
    curl -L ${GODOT_TEMPLATES_URL} -o /tmp/godot-templates.tpz && \
    # Create standard directories for Godot templates (system-wide path)
    mkdir -p /usr/local/share/godot/templates/${GODOT_VERSION}.stable && \
    # Unzip and install Godot binary
    unzip /tmp/godot-binary.zip -d /opt && \
    mv /opt/${GODOT_EXPECTED_BINARY} /usr/local/bin/godot && \
    chmod +x /usr/local/bin/godot && \
    # Unzip and install templates
    unzip /tmp/godot-templates.tpz -d /tmp && \
    mv /tmp/templates/* /usr/local/share/godot/templates/${GODOT_VERSION}.stable/ && \
    echo "Verifying Godot installation..." && \
    godot --version && \
    # Clean up downloaded files and temporary dependencies
    rm /tmp/godot-binary.zip && \
    rm /tmp/godot-templates.tpz && \
    rm -rf /tmp/templates

# --- Set GODOT_PATH Environment Variable ---

ENV GODOT_PATH=/usr/local/bin/godot

WORKDIR /gamenest

COPY target/gamenest-*.jar /gamenest/gamenest.jar

# Copy export preset config
COPY export ./export 

EXPOSE 8085

ENTRYPOINT ["java", "-jar", "/gamenest/gamenest.jar"]



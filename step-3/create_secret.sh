USER=`echo -n 'admin' | base64`
PASS=`echo -n 'password' | base64`
cat > secret.yaml <<EOF
apiVersion: v1
kind: Secret
metadata:
  name: mysecret
  namespace: liveproject
type: Opaque
data:
  username: $USER
  password: $PASS
EOF
kubectl apply -f secret.yaml

import { Link } from "react-router-dom";
import { Button } from "@/components/ui/button";
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card";
import {
  Field,
  FieldSet,
  FieldDescription,
  FieldGroup,
  FieldLabel,
  FieldError,
  FieldLegend,
  FieldSeparator,
} from "@/components/ui/field";
import { Input } from "@/components/ui/input";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { z } from "zod";
import HttpClient from "@/httpClient";
const Signup = () => {
  const schema = z
    .object({
      username: z
        .string()
        .trim()
        .min(5, "Must have atleast 5 characters")
        .max(20, "Must not exceed 20 characters"),
      password: z
        .string()
        .min(5, "Must have atleast 5 characters")
        .max(20, "Must not exceed 20 characters")
        .regex(/.*/, "Test"),
      confirmPassword: z.string().min(1, "Required"),
      address: z.string().trim().min(1, "Required"),
      aadhar: z
        .string()
        .trim()
        .min(1, "Required")
        .regex(/^[2-9][0-9]{11}$/, "Like 223905454312"),
      phone: z
        .string()
        .trim()
        .min(1, "Required")
        .regex(/^[6-9]\d{9}$/, "Like 9876543210"),
      dob: z
        .string()
        .trim()
        .min(1, "Required")
        .pipe(z.coerce.date({ invalid_type_error: "Invalid date format" }))
        .refine(
          (d) => {
            const today = new Date();
            const minDate = new Date(today.getFullYear() - 18, today.getMonth(), today.getDate());
            return d <= minDate;
          },
          { message: "Must be atleast 18 years old" },
        ),
      fullname: z
        .string()
        .trim()
        .min(1, "Required")
        .regex(/^[A-Z][a-z]*(?:\s+[A-Z][a-z]*)*$/, "Like Ram Mohan or R M Shyam"),
      email: z.string().trim().min(1, "Required").email(),
    })
    .refine((d) => d.password === d.confirmPassword, {
      message: "Passwords do not match",
      path: ["confirmPassword"],
    });

  const {
    register,
    handleSubmit,
    setError,
    clearErrors,
    formState: { errors, isValid, isSubmitting },
  } = useForm({ resolver: zodResolver(schema), mode: "onChange" });

  const signupHandler = async (data) => {
    try {
      const { username, password, address, aadhar, phone, fullname, email, dob } = data;
      const response = await HttpClient.post("/auth/is-username-available", {
        username: username,
      });
      console.log(response.data[username]);

      if (!response.data[username]) {
        setError("username", { type: "manual", message: "Username unavailable" });
        return;
      }

      const resp = await HttpClient.post("/customers/new-customer", {
        username: username,
        password: password,
        address: address,
        aadhar: aadhar,
        phone: phone,
        fullname: fullname,
        email: email,
        dob: dob,
      });
      if (resp.status != 200) {
        console.log(resp);
      }
    } catch (e) {
      console.log(e);
    }
  };

  const usernameProps = register("username");
  return (
    <div className="flex min-h-svh w-full items-center justify-center p-6 md:p-10">
      <Card className="w-full  max-w-sm shadow-mg">
        <CardContent>
          <div className="w-full ">
            <form onSubmit={handleSubmit(signupHandler)}>
              <FieldGroup>
                <FieldSet>
                  <FieldLegend>Login Details</FieldLegend>
                  <FieldGroup>
                    <Field data-invalid={errors.username != null}>
                      <FieldLabel htmlFor="name">username</FieldLabel>
                      <Input
                        id="name"
                        type="text"
                        placeholder="John Doe"
                        autoComplete="off"
                        required
                        {...usernameProps}
                        onChange={(e) => {
                          usernameProps.onChange(e);
                          clearErrors("username");
                        }}
                        aria-invalid={errors.username ? "true" : "false"}
                      />
                      {errors.username && <FieldError>{errors.username?.message}</FieldError>}
                    </Field>
                    <div className="grid grid-cols-2 gap-4">
                      <Field data-invalid={errors.password != null}>
                        <FieldLabel htmlFor="password">Password</FieldLabel>
                        <Input
                          id="password"
                          type="password"
                          required
                          aria-invalid={errors.password != null}
                          {...register("password", { deps: ["confirmPassword"] })}
                        />
                        {errors.password && <FieldError>{errors.password?.message}</FieldError>}
                      </Field>
                      <Field data-invalid={errors.confirmPassword != null}>
                        <FieldLabel htmlFor="confirm-password">Confirm Password</FieldLabel>
                        <Input
                          id="confirm-password"
                          type="password"
                          required
                          aria-invalid={errors.confirmPassword != null}
                          {...register("confirmPassword")}
                        />
                        {errors.confirmPassword && (
                          <FieldError>{errors.confirmPassword?.message}</FieldError>
                        )}
                      </Field>
                    </div>
                  </FieldGroup>
                </FieldSet>
                <FieldSet>
                  <FieldLegend>Profile</FieldLegend>
                  <FieldDescription>Please fill in your biographical details</FieldDescription>
                  <FieldGroup>
                    <Field data-invalid={errors.fullname != null}>
                      <FieldLabel htmlFor="fullname">Full Name</FieldLabel>
                      <Input
                        id="fullname"
                        type="text"
                        required
                        {...register("fullname")}
                        aria-invalid={errors.fullname != null}
                      />
                      {errors.fullname && <FieldError>{errors.fullname?.message}</FieldError>}
                    </Field>

                    <Field data-invalid={errors.aadhar != null}>
                      <FieldLabel htmlFor="aadhar">Aadhar</FieldLabel>
                      <Input
                        id="aadhar"
                        type="text"
                        required
                        {...register("aadhar")}
                        aria-invalid={errors.aadhar != null}
                      />
                      {errors.aadhar && <FieldError>{errors.aadhar?.message}</FieldError>}
                    </Field>
                    <Field data-invalid={errors.address != null}>
                      <FieldLabel htmlFor="address">Address</FieldLabel>
                      <Input
                        id="address"
                        type="text"
                        required
                        {...register("address")}
                        aria-invalid={errors.address != null}
                      />
                      {errors.address && <FieldError>{errors.address?.message}</FieldError>}
                    </Field>
                    <Field data-invalid={errors.email != null}>
                      <FieldLabel htmlFor="email">Email</FieldLabel>
                      <Input
                        id="email"
                        type="email"
                        required
                        {...register("email")}
                        aria-invalid={errors.email != null}
                      />
                      {errors.email && <FieldError>{errors.email?.message}</FieldError>}
                    </Field>
                    <div className="grid grid-cols-2 gap-4">
                      <Field data-invalid={errors.phone != null}>
                        <FieldLabel htmlFor="phone">Mobile Phone No.</FieldLabel>
                        <Input
                          id="phone"
                          type="text"
                          required
                          {...register("phone")}
                          aria-invalid={errors.phone != null}
                        />
                        {errors.phone && <FieldError>{errors.phone?.message}</FieldError>}
                      </Field>
                      <Field data-invalid={errors.dob != null}>
                        <FieldLabel htmlFor="dob">DOB</FieldLabel>
                        <Input
                          id="dob"
                          type="date"
                          required
                          {...register("dob")}
                          aria-invalid={errors.dob != null}
                        />
                        {errors.dob && <FieldError>{errors.dob?.message}</FieldError>}
                      </Field>
                    </div>
                    <Field>
                      <Button type="submit" disabled={!isValid || isSubmitting}>
                        Create Account
                      </Button>
                      <FieldDescription className="px-6 text-center">
                        Already have an account? <Link to="/">Sign in</Link>
                      </FieldDescription>
                    </Field>
                  </FieldGroup>
                </FieldSet>
              </FieldGroup>
            </form>
          </div>
        </CardContent>
      </Card>
    </div>
  );
};
export default Signup;

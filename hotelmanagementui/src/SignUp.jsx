import { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import { Button } from "@/components/ui/button";
import {
  Card,
  CardContent,
  CardDescription,
  CardHeader,
  CardTitle,
  CardFooter,
} from "@/components/ui/card";
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
import { Calendar } from "@/components/ui/calendar";
import { Input } from "@/components/ui/input";
import { Popover, PopoverContent, PopoverTrigger } from "@/components/ui/popover";
import { useForm, Controller } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { z } from "zod";
import HttpClient from "@/httpClient";
const Signup = () => {
  const [open, setOpen] = useState(false);
  const navigate = useNavigate();
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
        .any()
        .refine((v) => v instanceof Date, { message: "Required" })
        .refine(
          (d) => {
            const today = new Date();
            const minDate = new Date(today.getFullYear() - 18, today.getMonth(), today.getDate());
            return d <= minDate;
          },
          { message: "Must be at least 18 years old" },
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
    control,
    formState: { errors, isValid, isSubmitting },
  } = useForm({
    resolver: zodResolver(schema),
    mode: "onChange",
    defaultValues: {
      username: "",
      password: "",
      confirmPassword: "",
      fullname: "",
      aadhar: "",
      address: "",
      email: "",
      phone: "",
      dob: null,
    },
  });

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
    navigate("/", { replace: true });
  };

  return (
    <div className="flex min-h-svh w-full items-center justify-center p-6 md:p-10">
      <Card className="w-full  max-w-sm shadow-mg">
        <CardContent>
          <div className="w-full ">
            <form id="signup-form-id" onSubmit={handleSubmit(signupHandler)}>
              <FieldGroup>
                <FieldSet>
                  <FieldLegend>Login Details</FieldLegend>
                  <FieldDescription>
                    Please enter your desired username and password
                  </FieldDescription>
                  <FieldGroup>
                    <Controller
                      name="username"
                      control={control}
                      render={({ field, fieldState }) => (
                        <Field data-invalid={fieldState.invalid}>
                          <FieldLabel htmlFor="username">username</FieldLabel>
                          <Input
                            {...field}
                            id="username"
                            aria-invalid={fieldState.invalid}
                            autoComplete="off"
                          />
                          {fieldState.invalid && <FieldError errors={[fieldState.error]} />}
                        </Field>
                      )}
                    />
                    <div className="grid grid-cols-2 gap-4">
                      <Controller
                        name="password"
                        control={control}
                        rules={{ deps: ["confirmPassword"] }}
                        render={({ field, fieldState }) => (
                          <Field data-invalid={fieldState.invalid}>
                            <FieldLabel htmlFor="password">password</FieldLabel>
                            <Input
                              {...field}
                              type="password"
                              id="password"
                              aria-invalid={fieldState.invalid}
                            />
                            {fieldState.invalid && <FieldError errors={[fieldState.error]} />}
                          </Field>
                        )}
                      />
                      <Controller
                        name="confirmPassword"
                        control={control}
                        render={({ field, fieldState }) => (
                          <Field data-invalid={fieldState.invalid}>
                            <FieldLabel htmlFor="confirmPassword">confirm password</FieldLabel>
                            <Input
                              {...field}
                              type="password"
                              id="confirmPassword"
                              aria-invalid={fieldState.invalid}
                            />
                            {fieldState.invalid && <FieldError errors={[fieldState.error]} />}
                          </Field>
                        )}
                      />
                    </div>
                  </FieldGroup>
                </FieldSet>
                <FieldSet>
                  <FieldLegend>Profile</FieldLegend>
                  <FieldDescription>Please fill in your biographical details</FieldDescription>
                  <FieldGroup>
                    <Controller
                      name="fullname"
                      control={control}
                      render={({ field, fieldState }) => (
                        <Field data-invalid={fieldState.invalid}>
                          <FieldLabel htmlFor="fullname">fullname</FieldLabel>
                          <Input {...field} id="fullname" aria-invalid={fieldState.invalid} />
                          {fieldState.invalid && <FieldError errors={[fieldState.error]} />}
                        </Field>
                      )}
                    />
                    <Controller
                      name="aadhar"
                      control={control}
                      render={({ field, fieldState }) => (
                        <Field data-invalid={fieldState.invalid}>
                          <FieldLabel htmlFor="aadhar">aadhar</FieldLabel>
                          <Input {...field} id="aadhar" aria-invalid={fieldState.invalid} />
                          {fieldState.invalid && <FieldError errors={[fieldState.error]} />}
                        </Field>
                      )}
                    />
                    <Controller
                      name="address"
                      control={control}
                      render={({ field, fieldState }) => (
                        <Field data-invalid={fieldState.invalid}>
                          <FieldLabel htmlFor="address">address</FieldLabel>
                          <Input {...field} id="address" aria-invalid={fieldState.invalid} />
                          {fieldState.invalid && <FieldError errors={[fieldState.error]} />}
                        </Field>
                      )}
                    />
                    <Controller
                      name="email"
                      control={control}
                      render={({ field, fieldState }) => (
                        <Field data-invalid={fieldState.invalid}>
                          <FieldLabel htmlFor="email">Email</FieldLabel>
                          <Input
                            {...field}
                            type="email"
                            id="email"
                            aria-invalid={fieldState.invalid}
                          />
                          {fieldState.invalid && <FieldError errors={[fieldState.error]} />}
                        </Field>
                      )}
                    />
                    <div className="grid grid-cols-2 gap-4">
                      <Controller
                        name="phone"
                        control={control}
                        render={({ field, fieldState }) => (
                          <Field data-invalid={fieldState.invalid}>
                            <FieldLabel htmlFor="phone">Phone</FieldLabel>
                            <Input {...field} id="phone" aria-invalid={fieldState.invalid} />
                            {fieldState.invalid && <FieldError errors={[fieldState.error]} />}
                          </Field>
                        )}
                      />

                      <Controller
                        name="dob"
                        control={control}
                        render={({ field, fieldState }) => (
                          <Field data-invalid={fieldState.invalid}>
                            <FieldLabel htmlFor="dob">Date of Birth</FieldLabel>
                            <Popover open={open} onOpenChange={setOpen}>
                              <PopoverTrigger
                                render={
                                  <Button
                                    variant="outline"
                                    id="dob"
                                    className="justify-start font-normal"
                                  >
                                    {field.value ? field.value.toLocaleDateString() : "Select date"}
                                  </Button>
                                }
                              />
                              <PopoverContent className="w-auto overflow-hidden p-0" align="start">
                                <Calendar
                                  mode="single"
                                  selected={field.value || undefined}

                                  startMonth={new Date(new Date().getFullYear() - 100, 0)}
                                  endMonth={
                                    new Date(new Date().getFullYear() - 18, new Date().getMonth())
                                  }

                                  disabled={[
                                    { before: new Date(new Date().getFullYear() - 100, 0, 1) },
                                    {
                                      after: new Date(
                                        new Date().getFullYear() - 18,
                                        new Date().getMonth(),
                                        new Date().getDate(),
                                      ),
                                    },
                                  ]}
                                  defaultMonth={
                                    field.value ||
                                    (() => {
                                      return new Date(new Date().getFullYear() - 18, 0, 1);
                                    })()
                                  }
                                  captionLayout="dropdown"
                                  onSelect={(date) => {
                                    field.onChange(date);
                                    setOpen(false);
                                  }}
                                />
                              </PopoverContent>
                            </Popover>
                            {fieldState.invalid && <FieldError errors={[fieldState.error]} />}
                          </Field>
                        )}
                      />
                    </div>
                  </FieldGroup>
                </FieldSet>
              </FieldGroup>
            </form>
          </div>
        </CardContent>
        <CardFooter>
          <Field>
            <Button type="submit" disabled={!isValid || isSubmitting} form="signup-form-id">
              Create Account
            </Button>
            <FieldDescription className="px-6 text-center">
              Already have an account? <Link to="/">Sign in</Link>
            </FieldDescription>
          </Field>
        </CardFooter>
      </Card>
    </div>
  );
};
export default Signup;
